package com.triple.mileage.history;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointHistoryRepository extends JpaRepository<PointHistory,Long> {
    List<PointHistory> findAllByReviewIdAndUserId(String reviewId, String userId);
}
