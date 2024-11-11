package com.travel.demo.place.Service;

import com.travel.demo.place.model.PlaceDto;

public interface PlaceService {
    PlaceDto selectAll();
    void addContent(PlaceDto placeDto);
    void updateContent(PlaceDto placeDto);
}
