package com.travel.demo.plan.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(title="VisitPlace : 방문 장소 DTO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaceEditRequest {
    private Map<String, List<PlaceDTO>> dailySchedules;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PlaceDTO {
        private String id;
        private String displayName;
        private Location location;
        private String businessStatus;
        private String formattedAddress;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Location {
        private BigDecimal lat;
        private BigDecimal lng;
    }
}