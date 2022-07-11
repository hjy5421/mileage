package com.triple.mileage.rest;

import com.triple.mileage.history.PointHistoryService;
import com.triple.mileage.photo.PhotoService;
import com.triple.mileage.place.PlaceService;
import com.triple.mileage.review.ReviewService;
import com.triple.mileage.review.request.Action;
import com.triple.mileage.review.request.ReviewRequest;
import com.triple.mileage.review.request.Type;
import com.triple.mileage.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@RunWith(MockitoJUnitRunner.class)
public class MileageControllerTest {
    @InjectMocks
    private MileageController mileageController;
    @Mock
    private UserService userService;
    @Mock
    private PlaceService placeService;
    @Mock
    private PhotoService photoService;
    @Mock
    private ReviewService reviewService;
    @Mock
    private PointHistoryService pointHistoryService;

    private final String userId = "userId";
    private final String reviewId = "reviewId";
    private final String content = "content";
    private final List<String> attachedPhotoIds = List.of("photoId1", "photoId2");
    private final String placeId = "placeId";

    private ReviewRequest makeReviewRequest(Type type, Action action) {
        return new ReviewRequest(type, action, reviewId, content, attachedPhotoIds, userId, placeId);
    }

    @Before
    public void setUp() throws Exception {
        given(userService.existsByUserId(userId)).willReturn(true);
        given(placeService.existsByPlaceId(placeId)).willReturn(true);
        given(reviewService.existsByReviewId(reviewId)).willReturn(true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void postEvents_createReview_IllegalUserId() {
        // given
        given(userService.existsByUserId(userId)).willReturn(false);
        ReviewRequest reviewRequest = makeReviewRequest(Type.REVIEW, Action.ADD);

        // when
        mileageController.postEvents(reviewRequest);

        // then
    }

    @Test(expected = IllegalArgumentException.class)
    public void postEvents_createReview_IllegalPlaceId() {
        // given
        given(placeService.existsByPlaceId(placeId)).willReturn(false);
        ReviewRequest reviewRequest = makeReviewRequest(Type.REVIEW, Action.ADD);

        // when
        mileageController.postEvents(reviewRequest);

        // then
    }

    @Test
    public void postEvents_createReview() {
        // given
        ReviewRequest reviewRequest = makeReviewRequest(Type.REVIEW, Action.ADD);
        int addPoints = 123;
        given(reviewService.findAddChangePoint(reviewRequest)).willReturn(addPoints);

        // when
        mileageController.postEvents(reviewRequest);

        // then
        then(reviewService).should().createReview(reviewRequest);
        then(photoService).should().createPhotos(reviewId, attachedPhotoIds);
        then(userService).should().updateUserPoint(userId, addPoints);
        then(pointHistoryService).should().createPointHistory(reviewRequest, addPoints);
    }

    @Test(expected = IllegalArgumentException.class)
    public void postEvents_updateReview_IllegalUserId(){
        // given
        given(userService.existsByUserId(userId)).willReturn(false);
        ReviewRequest reviewRequest = makeReviewRequest(Type.REVIEW, Action.MOD);

        // when
        mileageController.postEvents(reviewRequest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void postEvents_updateReview_IllegalPlaceId(){
        // given
        given(placeService.existsByPlaceId(placeId)).willReturn(false);
        ReviewRequest reviewRequest = makeReviewRequest(Type.REVIEW, Action.MOD);

        // when
        mileageController.postEvents(reviewRequest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void postEvents_updateReview_IllegalReviewId(){
        // given
        given(reviewService.existsByReviewId(reviewId)).willReturn(false);
        ReviewRequest reviewRequest = makeReviewRequest(Type.REVIEW, Action.MOD);

        // when
        mileageController.postEvents(reviewRequest);
    }

    @Test
    public void postEvents_updateReview() {
        //given
        ReviewRequest reviewRequest = makeReviewRequest(Type.REVIEW, Action.MOD);
        int modPoints = 1;
        given(reviewService.existsContent(reviewId)).willReturn(true);
        given(photoService.existsPhoto(reviewId)).willReturn(true);
        given(reviewService.findModChangePoint(reviewRequest, true, true)).willReturn(modPoints);

        //when
        mileageController.postEvents(reviewRequest);

        //then
        then(reviewService).should().updateReview(reviewRequest);
        then(photoService).should().updatePhotos(reviewId, attachedPhotoIds);
        then(userService).should().updateUserPoint(userId, modPoints);
        then(pointHistoryService).should().createPointHistory(reviewRequest, modPoints);
    }

    @Test(expected = IllegalArgumentException.class)
    public void postEvents_deleteReview_IllegalUserId(){
        // given
        given(userService.existsByUserId(userId)).willReturn(false);
        ReviewRequest reviewRequest = makeReviewRequest(Type.REVIEW, Action.DELETE);

        // when
        mileageController.postEvents(reviewRequest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void postEvents_deleteReview_IllegalPlaceId(){
        // given
        given(placeService.existsByPlaceId(placeId)).willReturn(false);
        ReviewRequest reviewRequest = makeReviewRequest(Type.REVIEW, Action.DELETE);

        // when
        mileageController.postEvents(reviewRequest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void postEvents_deleteReview_IllegalReviewId(){
        // given
        given(reviewService.existsByReviewId(reviewId)).willReturn(false);
        ReviewRequest reviewRequest = makeReviewRequest(Type.REVIEW, Action.DELETE);

        // when
        mileageController.postEvents(reviewRequest);
    }

    @Test
    public void postEvents_delteReview(){
        //given
        ReviewRequest reviewRequest=makeReviewRequest(Type.REVIEW,Action.DELETE);
        int deletePoint=3;
        given(pointHistoryService.findDeleteChangePoint(reviewRequest)).willReturn(deletePoint);

        //when
        mileageController.postEvents(reviewRequest);

        //then
        then(userService).should().updateUserPoint(userId,-deletePoint);
        then(pointHistoryService).should().createPointHistory(reviewRequest,-deletePoint);
        then(reviewService).should().deleteReview(reviewRequest);
        then(photoService).should().deletePhotos(reviewId);
    }
}