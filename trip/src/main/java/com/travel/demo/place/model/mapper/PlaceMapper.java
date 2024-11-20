package com.travel.demo.place.model.mapper;

import com.travel.demo.place.dto.Place;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PlaceMapper {
    Place selectAll();
}
