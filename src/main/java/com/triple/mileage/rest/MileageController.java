package com.triple.mileage.rest;

import com.triple.mileage.dto.Result;
import com.triple.mileage.history.PointHistoryService;
import com.triple.mileage.photo.PhotoService;
import com.triple.mileage.place.PlaceService;
import com.triple.mileage.review.ReviewService;
import com.triple.mileage.review.request.Action;
import com.triple.mileage.review.request.ReviewRequest;
import com.triple.mileage.user.User;
import com.triple.mileage.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
public class MileageController {
    private static final Logger log = LoggerFactory.getLogger(MileageController.class);

    private final UserService userService;
    private final PlaceService placeService;
    private final PhotoService photoService;
    private final ReviewService reviewService;
    private final PointHistoryService pointHistoryService;

    public MileageController(UserService userService, PlaceService placeService, PhotoService photoService, ReviewService reviewService, PointHistoryService pointHistoryService) {
        this.userService = userService;
        this.placeService = placeService;
        this.photoService = photoService;
        this.reviewService = reviewService;
        this.pointHistoryService = pointHistoryService;
    }

    /**
     * review
     *
     * @param reviewRequest
     * @return
     */
    @PostMapping("/events")
    public Result postEvents(@RequestBody ReviewRequest reviewRequest) {
        reviewRequest.verify();

        if (reviewRequest.getAction() == Action.ADD) {
            createReview(reviewRequest);
        } else if (reviewRequest.getAction() == Action.MOD) {
            updateReview(reviewRequest);
        } else if (reviewRequest.getAction() == Action.DELETE) {
            deleteReview(reviewRequest);
        } else {
            // 미지원 Action
            throw new IllegalArgumentException("unsupported action");
        }

        return Result.success();
    }

    /**
     * @param userId
     * @return
     */
    @GetMapping("/points")
    public Result getPoints(@RequestParam String userId) {
        User user = userService.getUser(userId);
        return Result.success(user);
    }

    /**
     * Delete Review
     *
     * @param reviewRequest
     */
    private void deleteReview(ReviewRequest reviewRequest) {
        if (!isUserIdValid(reviewRequest.getUserId()) || !isPlaceIdValid(reviewRequest.getPlaceId()) || !isReviewIdValid(reviewRequest.getReviewId())) {
            throw new IllegalArgumentException("invalid id");
        }

        int changePoint = pointHistoryService.findDeleteChangePoint(reviewRequest);

        if (changePoint != 0) {
            userService.updateUserPoint(reviewRequest.getUserId(), -changePoint);
            pointHistoryService.createPointHistory(reviewRequest, -changePoint);
        }
        reviewService.deleteReview(reviewRequest);
        photoService.deletePhotos(reviewRequest.getReviewId());
    }

    /**
     * Update Review
     *
     * @param reviewRequest
     */
    private void updateReview(ReviewRequest reviewRequest) {
        if (!isUserIdValid(reviewRequest.getUserId()) || !isPlaceIdValid(reviewRequest.getPlaceId()) || !isReviewIdValid(reviewRequest.getReviewId())) {
            throw new IllegalArgumentException("invalid id");
        }

        boolean isExistOriginContent = reviewService.existsContent(reviewRequest.getReviewId());
        boolean isExistOriginPhoto = photoService.existsPhoto(reviewRequest.getReviewId());

        int changePoint = reviewService.findModChangePoint(reviewRequest, isExistOriginContent, isExistOriginPhoto);
        reviewService.updateReview(reviewRequest);
        photoService.updatePhotos(reviewRequest.getReviewId(), reviewRequest.getAttachedPhotoIds());

        if (changePoint != 0) {
            userService.updateUserPoint(reviewRequest.getUserId(), changePoint);
            pointHistoryService.createPointHistory(reviewRequest, changePoint);
        }
    }

    /**
     * Create Review
     *
     * @param reviewRequest
     */
    private void createReview(ReviewRequest reviewRequest) {
        if (!isUserIdValid(reviewRequest.getUserId()) || !isPlaceIdValid(reviewRequest.getPlaceId())) {
            throw new IllegalArgumentException("invalid id");
        }

        //한 사용자마다 장소마다 리뷰 1개만 가능
        if(reviewService.existsByUserIdAndPlaceId(reviewRequest.getUserId(),reviewRequest.getPlaceId())){
            throw new IllegalArgumentException("duplicate id");
        }

        reviewService.createReview(reviewRequest);
        photoService.createPhotos(reviewRequest.getReviewId(), reviewRequest.getAttachedPhotoIds());

        int changePoint = reviewService.findAddChangePoint(reviewRequest);

        if (changePoint != 0) {
            userService.updateUserPoint(reviewRequest.getUserId(), changePoint);
            pointHistoryService.createPointHistory(reviewRequest, changePoint);
        }
    }

    private boolean isUserIdValid(String userId) {
        return userService.existsByUserId(userId);
    }

    private boolean isPlaceIdValid(String placeId) {
        return placeService.existsByPlaceId(placeId);
    }

    private boolean isReviewIdValid(String reviewId) {
        return reviewService.existsByReviewId(reviewId);
    }
}
