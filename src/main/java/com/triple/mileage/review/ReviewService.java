package com.triple.mileage.review;

import com.triple.mileage.review.request.ReviewRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;


@Service
public class ReviewService {
    private static final Logger log = LoggerFactory.getLogger(ReviewService.class);
    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    /*
    리뷰 생성
     */
    public Review createReview(ReviewRequest reviewRequest) {
        Review review = new Review(reviewRequest.getReviewId(),
                reviewRequest.getUserId(),
                reviewRequest.getPlaceId(),
                reviewRequest.getContent());
        return reviewRepository.save(review);
    }

    /*
    리뷰 수정
     */
    public Review updateReview(ReviewRequest reviewRequest) {
        Review origin = reviewRepository.findByReviewId(reviewRequest.getReviewId());

        if (!origin.getUserId().equals(reviewRequest.getUserId())) {
            log.error("{} : Incorrect userId {}", log.getName(), reviewRequest.getPlaceId());
            throw new IllegalArgumentException("Incorrect userId");
        }

        if (!origin.getPlaceId().equals(reviewRequest.getPlaceId())) {
            log.error("{} : Incorrect placeId {}", log.getName(), reviewRequest.getPlaceId());
            throw new IllegalArgumentException("Incorrect placeId");
        }

        origin.setContent(reviewRequest.getContent());
        return reviewRepository.save(origin);
    }

    /*
    리뷰 삭제
     */
    public void deleteReview(ReviewRequest reviewRequest) {
        Review origin = reviewRepository.findByReviewId(reviewRequest.getReviewId());
        reviewRepository.delete(origin);
    }

    /*
    리뷰 생성 시 부여할 점수 확인
     */
    public int findAddChangePoint(ReviewRequest reviewRequest) {
        int changePoint = 0;
        String content = reviewRequest.getContent();
        if (StringUtils.hasText(content)) {
            changePoint++;
        }

        List<String> attachedPhotoIds = reviewRequest.getAttachedPhotoIds();
        if (!CollectionUtils.isEmpty(attachedPhotoIds)) {
            changePoint++;
        }

        Review firstReview = reviewRepository.findFirstByPlaceIdOrderByCreateDateAsc(reviewRequest.getPlaceId());
        if (reviewRequest.getUserId().equals(firstReview.getUserId())) {
            changePoint++;
        }
        return changePoint;
    }

    /**
     * 리뷰 수정 시 부여할 점수 확인
     *
     * @param reviewRequest
     * @param isExistOriginContent
     * @param isExistOriginPhoto
     * @return
     */
    public int findModChangePoint(ReviewRequest reviewRequest, boolean isExistOriginContent, boolean isExistOriginPhoto) {
        int changePoint = 0;

        List<String> attachedPhotoIds = reviewRequest.getAttachedPhotoIds();
        //글만 작성한 리뷰에 사진 추가 시
        if (isExistOriginContent && !isExistOriginPhoto) {
            if (!CollectionUtils.isEmpty(attachedPhotoIds)) {
                changePoint++;
            }
        }

        //글과 사진이 있는 리뷰에 사진을 모두 삭제 시
        if (isExistOriginContent && isExistOriginPhoto) {
            if (CollectionUtils.isEmpty(attachedPhotoIds)) {
                changePoint--;
            }
        }
        return changePoint;
    }

    /*
    리뷰에 글이 있는지 확인
     */
    public boolean existsContent(String reviewId) {
        Review origin = reviewRepository.findByReviewId(reviewId);
        if (StringUtils.hasText(origin.getContent())) {
            return true;
        }
        return false;
    }

    public boolean existsByReviewId(String reviewId) {
        return reviewRepository.existsByReviewId(reviewId);
    }

    public boolean existsByUserIdAndPlaceId(String userId, String placeId) {
        return reviewRepository.existsByUserIdAndPlaceId(userId,placeId);
    }
}
