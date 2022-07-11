package com.triple.mileage.history;

import com.triple.mileage.review.Review;
import com.triple.mileage.user.User;
import lombok.Getter;

import javax.persistence.*;


@Entity
@Table(indexes = {
        @Index(columnList = "userId"),
})
public class PointHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String reviewId;

    @Column(nullable = false)
    private int changePoint;

    public PointHistory() {
    }

    public PointHistory(String userId, String reviewId, int changePoint) {
        this.userId = userId;
        this.reviewId = reviewId;
        this.changePoint = changePoint;
    }

    public String getUserId() {
        return userId;
    }

    public String getReviewId() {
        return reviewId;
    }

    public int getChangePoint() {
        return changePoint;
    }
}
