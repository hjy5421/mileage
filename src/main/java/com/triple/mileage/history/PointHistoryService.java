package com.triple.mileage.history;

import com.triple.mileage.review.request.ReviewRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointHistoryService {
    private final PointHistoryRepository pointHistoryRepository;

    public PointHistoryService(PointHistoryRepository pointHistoryRepository) {
        this.pointHistoryRepository = pointHistoryRepository;
    }

    /*
    포인트 이력 생성
     */
    public PointHistory createPointHistory(ReviewRequest reviewRequest, int changePoint) {
        PointHistory pointHistory = new PointHistory(reviewRequest.getUserId(), reviewRequest.getReviewId(), changePoint);
        return pointHistoryRepository.save(pointHistory);
    }

    /*
    리뷰 생성 시, 내용 점수와 보너스 점수 회수
     */
    public int findDeleteChangePoint(ReviewRequest reviewRequest) {
        List<PointHistory> pointHistories = pointHistoryRepository.findAllByReviewIdAndUserId(reviewRequest.getReviewId(), reviewRequest.getUserId());
        return pointHistories.stream().mapToInt(PointHistory::getChangePoint).sum();
    }
}
