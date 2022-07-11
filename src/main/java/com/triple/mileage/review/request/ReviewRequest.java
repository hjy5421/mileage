package com.triple.mileage.review.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

public class ReviewRequest {
    private final Type type;
    private final Action action;
    private final String reviewId;
    private final String content;
    private final List<String> attachedPhotoIds;
    private final String userId;
    private final String placeId;

    @JsonCreator
    public ReviewRequest(Type type, Action action, String reviewId, String content, List<String> attachedPhotoIds, String userId, String placeId) {
        this.type = type;
        this.action = action;
        this.reviewId = reviewId;
        this.content = content;
        this.attachedPhotoIds = attachedPhotoIds;
        this.userId = userId;
        this.placeId = placeId;
    }

    public Type getType() {
        return type;
    }

    public Action getAction() {
        return action;
    }

    public String getReviewId() {
        return reviewId;
    }

    public String getContent() {
        return content;
    }

    public List<String> getAttachedPhotoIds() {
        return attachedPhotoIds;
    }

    public String getUserId() {
        return userId;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void verify() {
        if (!StringUtils.hasText(this.getReviewId()) || !StringUtils.hasText(this.getUserId()) || !StringUtils.hasText(this.getPlaceId())) {
            throw new IllegalArgumentException();
        }

        if (!StringUtils.hasText(this.getContent()) && CollectionUtils.isEmpty(this.attachedPhotoIds)) {
            throw new IllegalArgumentException();
        }

        if (this.getType() != Type.REVIEW) {
            throw new IllegalArgumentException();
        }
    }
}
