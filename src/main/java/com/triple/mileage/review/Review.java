package com.triple.mileage.review;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(indexes = {
        @Index(columnList = "userId, placeId")
})
public class Review {
    @Id
    @Column(nullable = false)
    private String reviewId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String placeId;

    @Lob
    @Column(nullable = false)
    private String content;

    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime modifyDate;

    public Review() { }

    public Review(String reviewId, String userId, String placeId, String content) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.placeId = placeId;
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
