package com.travel.batch;

import com.travel.place.mapper.PlaceMapper;
import com.travel.place.model.PlaceDto;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


public class ApiDataWriter implements ItemWriter<PlaceDto> {

    @Autowired
    private PlaceMapper placeMapper;

    @Override
    public void write(List<? extends PlaceDto> items) throws Exception {
        for (PlaceDto placeDto : items) {
            placeMapper.updatePlace(placeDto); // DB에 업데이트
        }
    }

    @Override
    public void write(Chunk<? extends PlaceDto> chunk) throws Exception {

    }
}