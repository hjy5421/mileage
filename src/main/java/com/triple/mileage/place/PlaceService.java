package com.triple.mileage.place;

import org.springframework.stereotype.Service;

@Service
public class PlaceService {
    private final PlaceRepository placeRepository;

    public PlaceService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public boolean existsByPlaceId(String placeId) {
        return placeRepository.existsByPlaceId(placeId);
    }
}
