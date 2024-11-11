package com.travel.demo.place.Service;

import com.travel.demo.place.mapper.PlaceMapper;
import com.travel.demo.place.model.PlaceDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PlaceServiceImpl implements  PlaceService {

    private final PlaceMapper placeMapper;

    public PlaceServiceImpl(PlaceMapper placeMapper) {
        this.placeMapper = placeMapper;
    }

    @Override
    public PlaceDto selectAll() {
        return placeMapper.selectAll();
    }

    @Override
    public void addContent(PlaceDto placeDto) {
        placeMapper.addContent(placeDto);
    }

    @Override
    public void updateContent(PlaceDto placeDto) {
        placeMapper.updateContent(placeDto);
    }
}
