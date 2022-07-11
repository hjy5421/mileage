package com.triple.mileage.place;

import javax.persistence.*;


@Entity
public class Place {
    @Id
    @Column(nullable = false)
    private String placeId;

    public Place() {
    }

    public String getPlaceId() {
        return placeId;
    }
}
