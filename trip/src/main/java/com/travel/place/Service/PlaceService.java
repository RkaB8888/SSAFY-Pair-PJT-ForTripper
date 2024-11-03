package com.travel.place.Service;

import com.travel.place.model.PlaceDto;

public interface PlaceService {
    PlaceDto selectAll();
    void addContent(PlaceDto placeDto);
    void updateContent(PlaceDto placeDto);
}
