package com.travel.batch;

import com.travel.place.mapper.PlaceMapper;
import com.travel.place.model.PlaceDto;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ApiDataProcessor implements ItemProcessor<PlaceDto, PlaceDto> {

    private final PlaceMapper placeMapper;

    @Autowired
    public ApiDataProcessor(PlaceMapper placeMapper) {
        this.placeMapper = placeMapper;
    }

    @Override
    public PlaceDto process(PlaceDto placeDto) throws Exception {
        PlaceDto existingData = placeMapper.findById(placeDto.getPlace_id());
        if (existingData != null && !existingData.getModified_time().equals(placeDto.getModified_time())) {
            return placeDto; // 수정일이 다르면 업데이트 대상
        }
        return null; // 수정일이 동일하거나 존재하지 않으면 null 반환
    }
}