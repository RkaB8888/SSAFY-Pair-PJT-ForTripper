package com.travel.demo.place.model.service;

import com.travel.demo.place.model.mapper.PlaceMapper;
import com.travel.demo.place.dto.Place;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements  PlaceService {

    private final PlaceMapper placeMapper;

    @Override
    public Place selectAll() {
        return placeMapper.selectAll();
    }
}
