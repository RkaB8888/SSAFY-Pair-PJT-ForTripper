package com.travel.place.mapper;

import com.travel.place.model.PlaceDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PlaceMapper {
    PlaceDto selectAll();
    void addContent(PlaceDto placeDto);
    void updateContent(PlaceDto placeDto);
}
