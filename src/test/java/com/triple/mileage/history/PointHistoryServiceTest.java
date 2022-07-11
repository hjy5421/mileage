package com.triple.mileage.history;

import com.triple.mileage.review.request.Action;
import com.triple.mileage.review.request.ReviewRequest;
import com.triple.mileage.review.request.Type;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@RunWith(MockitoJUnitRunner.class)
public class PointHistoryServiceTest {
    @InjectMocks
    private PointHistoryService pointHistoryService;
    @Mock
    private PointHistoryRepository pointHistoryRepository;

    private final Type type = Type.REVIEW;
    private final String userId = "userId";
    private final String reviewId = "reviewId";
    private final String content = "content";
    private final List<String> attachedPhotoIds = List.of("photoId1", "photoId2");
    private final String placeId = "placeId";

    private ReviewRequest makeReviewRequest(Action action) {
        return new ReviewRequest(type, action, reviewId, content, attachedPhotoIds, userId, placeId);
    }

    private PointHistory makePointHistory(int changePoint) {
        return new PointHistory(userId, reviewId, changePoint);
    }

    @Test
    public void createPointHistory() {
        //given
        ReviewRequest reviewRequest = makeReviewRequest(Action.ADD);
        int changePoint = 123;
        PointHistory expected = makePointHistory(changePoint);
        given(pointHistoryRepository.save(any())).willReturn(expected);

        //when
        PointHistory actual = pointHistoryService.createPointHistory(reviewRequest, changePoint);

        //then
        assertEquals(userId, actual.getUserId());
        assertEquals(reviewId, actual.getReviewId());
        assertEquals(changePoint, actual.getChangePoint());
    }

    @Test
    public void findDeleteChangePoint() {
        //given
        ReviewRequest reviewRequest = makeReviewRequest(Action.DELETE);
        int expected = 3;
        List<PointHistory> pointHistories = List.of(makePointHistory(1), makePointHistory(2));
        given(pointHistoryRepository.findAllByReviewIdAndUserId(reviewId, userId)).willReturn(pointHistories);

        //when
        int actual = pointHistoryService.findDeleteChangePoint(reviewRequest);

        //then
        assertEquals(expected, actual);
    }
}