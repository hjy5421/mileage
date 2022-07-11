package com.triple.mileage.review.request;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Locale;

public enum Action {
    ADD,
    MOD,
    DELETE;

    @JsonCreator
    public static Action from(String action){
        return Action.valueOf(action.toUpperCase(Locale.ROOT));
    }
}
