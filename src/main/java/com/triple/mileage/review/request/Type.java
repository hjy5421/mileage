package com.triple.mileage.review.request;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Locale;

public enum Type {
    REVIEW;

    @JsonCreator
    public static Type from(String type){
        return Type.valueOf(type.toUpperCase(Locale.ROOT));
    }


}
