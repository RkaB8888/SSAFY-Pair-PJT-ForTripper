package com.travel.demo.shareBoard.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ShareAddRequestDTO2 {
    private FormData formData;

    @JsonRawValue
    private Schedules dailySchedules;
    private int totalDate;

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FormData {
        private String title;
        private String content;
        private MultipartFile image;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Schedules {
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
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Location {
            private BigDecimal lat;
            private BigDecimal lng;
        }
    }
}
