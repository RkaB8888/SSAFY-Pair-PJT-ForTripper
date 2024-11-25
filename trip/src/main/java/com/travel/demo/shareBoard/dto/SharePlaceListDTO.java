package com.travel.demo.shareBoard.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(title="VisitPlace : 방문 장소 DTO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SharePlaceListDTO {
    private Map<String, List<PlaceDTO>> dailySchedules = new HashMap<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PlaceDTO {
        private String id;
        private String displayName;
        private Location location;
        private String formattedAddress;
        private String internationalPhoneNumber;
        private int visit_order;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Location {
        private BigDecimal lat;
        private BigDecimal lng;
    }
}
