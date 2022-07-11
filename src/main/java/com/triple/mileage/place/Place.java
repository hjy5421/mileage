package com.triple.mileage.place;

import javax.persistence.*;


@Entity
@Table(indexes = @Index(name = "place_id", columnList = "placeId",unique = true))
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
