package com.travel.batch;

import com.travel.place.model.PlaceDto;
import org.springframework.batch.item.ItemReader;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Iterator;

public class ApiDataReader implements ItemReader<PlaceDto> {

    private final RestTemplate restTemplate;
    private final String apiUrl;
    private Iterator<PlaceDto> placeDtoIterator;

    public ApiDataReader(RestTemplate restTemplate, String apiUrl) {
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
    }

    @Override
    public PlaceDto read() throws Exception {
        if (placeDtoIterator == null || !placeDtoIterator.hasNext()) {
            PlaceDto[] apiPlaces = restTemplate.getForObject(apiUrl, PlaceDto[].class);
            if (apiPlaces != null) {
                placeDtoIterator = Arrays.asList(apiPlaces).iterator();
            }
        }
        return placeDtoIterator != null && placeDtoIterator.hasNext() ? placeDtoIterator.next() : null;
    }
}