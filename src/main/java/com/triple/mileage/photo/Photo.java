package com.triple.mileage.photo;

import javax.persistence.*;

@Entity
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
