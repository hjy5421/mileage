package com.triple.mileage.review;

import com.triple.mileage.review.request.Action;
import com.triple.mileage.review.request.ReviewRequest;
import com.triple.mileage.review.request.Type;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@RunWith(MockitoJUnitRunner.class)
public class ReviewServiceTest {
    @InjectMocks
    private ReviewService reviewService;
    @Mock
    private ReviewRepository reviewRepository;
    @Captor
    private ArgumentCaptor<Review> reviewArgumentCaptor;

    private final Type type = Type.REVIEW;
    private final String userId = "userId";
    private final String reviewId = "reviewId";
    private final String content = "content";
    private final List<String> attachedPhotoIds = List.of("photoId1", "photoId2");
    private final String placeId = "placeId";

    private ReviewRequest makeReviewRequest(Action action) {
        return new ReviewRequest(type, action, reviewId, content, attachedPhotoIds, userId, placeId);
    }

    private ReviewRequest makeReviewRequest(Action action, String content) {
        return new ReviewRequest(type, action, reviewId, content, attachedPhotoIds, userId, placeId);
    }

    private ReviewRequest makeReviewRequest(Action action, List<String> attachedPhotoIds) {
        return new ReviewRequest(type, action, reviewId, content, attachedPhotoIds, userId, placeId);
    }

    private Review makeReview() {
        return new Review(reviewId, userId, placeId, content);
    }

    private Review makeReview(String content) {
        return new Review(reviewId, userId, placeId, content);
    }

    @Test
    public void createReview() {
        //given
        ReviewRequest reviewRequest = makeReviewRequest(Action.ADD);
        Review expected = makeReview();
        given(reviewRepository.save(any())).willReturn(expected);

        //when
        Review actual = reviewService.createReview(reviewRequest);

        //then
//        then(reviewRepository).should().save(reviewArgumentCaptor.capture());
//        Review tempReview = reviewArgumentCaptor.getValue();
//
//        assertEquals(userId, tempReview.getUserId());
//        assertEquals(placeId, tempReview.getPlaceId());
//        assertEquals(content, tempReview.getContent());

        assertEquals(userId, actual.getUserId());
        assertEquals(placeId, actual.getPlaceId());
        assertEquals(content, actual.getContent());
    }

    @Test
    public void updateReview() {
        //given
        ReviewRequest reviewRequest = makeReviewRequest(Action.MOD);
        Review origin = makeReview("originContent");
        given(reviewRepository.findByReviewId(any())).willReturn(origin);
        Review expected = makeReview();
        given(reviewRepository.save(any())).willReturn(expected);

        //when
        Review actual = reviewService.updateReview(reviewRequest);

        //then
        assertEquals(userId, actual.getUserId());
        assertEquals(placeId, actual.getPlaceId());
        assertEquals(content, actual.getContent());
    }

    //TODO : 삭제 테스트 잘 모르겠음...
    @Test
    public void deleteReview() {
        //given
        ReviewRequest reviewRequest = makeReviewRequest(Action.DELETE);
        Review origin = makeReview();
        given(reviewRepository.findByReviewId(eq(reviewId))).willReturn(origin);

        //when
        reviewService.deleteReview(reviewRequest);

        //then
        assertFalse(reviewRepository.existsByReviewId(reviewId));
    }

    @Test
    public void findAddChangePoint_AttachedPhotoIds() {
        //given
        ReviewRequest reviewRequest = makeReviewRequest(Action.ADD, List.of());
        int expectedPoint = 2;
        Review review = makeReview();
        given(reviewRepository.findFirstByPlaceIdOrderByCreateDateAsc(any())).willReturn(review);

        //when
        int actualPoint = reviewService.findAddChangePoint(reviewRequest);

        //then
        assertEquals(expectedPoint, actualPoint);
    }

    @Test
    public void findAddChangePoint_Content() {
        //given
        ReviewRequest reviewRequest = makeReviewRequest(Action.ADD, "");
        int expectedPoint = 2;
        Review review = makeReview("");
        given(reviewRepository.findFirstByPlaceIdOrderByCreateDateAsc(any())).willReturn(review);

        //when
        int actualPoint = reviewService.findAddChangePoint(reviewRequest);

        //then
        assertEquals(expectedPoint, actualPoint);
    }

    @Test
    public void findAddChangePoint() {
        //given
        ReviewRequest reviewRequest = makeReviewRequest(Action.ADD);
        int expectedPoint = 3;
        Review review = makeReview();
        given(reviewRepository.findFirstByPlaceIdOrderByCreateDateAsc(any())).willReturn(review);

        //when
        int actualPoint = reviewService.findAddChangePoint(reviewRequest);

        //then
        assertEquals(expectedPoint, actualPoint);
    }

    @Test
    public void findModChangePoint_EraseAllPhotos() {
        //given
        ReviewRequest reviewRequest = makeReviewRequest(Action.MOD,List.of());
        int expectedPoint = -1;
        boolean isExistOriginContent = true;
        boolean isExistOriginPhoto = true;

        //when
        int actualPoint = reviewService.findModChangePoint(reviewRequest, isExistOriginContent, isExistOriginPhoto);

        //then
        assertEquals(expectedPoint, actualPoint);
    }

    @Test
    public void findModChangePoint_OnlyContent() {
        //given
        ReviewRequest reviewRequest = makeReviewRequest(Action.MOD);
        int expectedPoint = 1;
        boolean isExistOriginContent = true;
        boolean isExistOriginPhoto = false;

        //when
        int actualPoint = reviewService.findModChangePoint(reviewRequest, isExistOriginContent, isExistOriginPhoto);

        //then
        assertEquals(expectedPoint, actualPoint);
    }

    @Test
    public void findModChangePoint() {
        //given
        ReviewRequest reviewRequest = makeReviewRequest(Action.MOD);
        int expectedPoint = 0;
        boolean isExistOriginContent = true;
        boolean isExistOriginPhoto = true;

        //when
        int actualPoint = reviewService.findModChangePoint(reviewRequest, isExistOriginContent, isExistOriginPhoto);

        //then
        assertEquals(expectedPoint, actualPoint);
    }

    @Test
    public void existsContent() {
        //given
        ReviewRequest reviewRequest=makeReviewRequest(Action.MOD);
        Review review=makeReview();
        boolean expected=true;
        given(reviewRepository.findByReviewId(any())).willReturn(review);

        //when
        boolean actual=reviewService.existsContent(reviewRequest.getReviewId());

        //then
        assertEquals(expected,actual);
    }

    @Test
    public void existsByReviewId() {
        //given
        ReviewRequest reviewRequest=makeReviewRequest(Action.MOD);
        boolean expected=true;
        given(reviewRepository.existsByReviewId(any())).willReturn(true);

        //when
        boolean actual=reviewService.existsByReviewId(reviewRequest.getReviewId());

        //then
        assertEquals(expected,actual);
    }
}