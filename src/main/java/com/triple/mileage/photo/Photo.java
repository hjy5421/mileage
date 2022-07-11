package com.triple.mileage.photo;

import javax.persistence.*;

@Entity
@Table(indexes = {
        @Index(name = "photo_id", columnList = "photoId", unique = true),
        @Index(name = "review_id", columnList = "reviewId")
})
public class Photo {
    @Id
    @Column(nullable = false)
    private String photoId;

    @Column(nullable = false)
    private String reviewId;

    public Photo() {
    }

    public Photo(String photoId, String reviewId) {
        this.photoId = photoId;
        this.reviewId = reviewId;
    }

    public String getPhotoId() {
        return photoId;
    }

    public String getReviewId() {
        return reviewId;
    }
}
