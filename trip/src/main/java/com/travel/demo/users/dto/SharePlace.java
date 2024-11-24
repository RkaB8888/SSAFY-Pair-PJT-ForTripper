package com.travel.demo.users.dto;

import lombok.Data;

@Data
public class SharePlace {
    private String id;
    private String displayName;
    private Location location;
    private String formattedAddress;
    private String internationalPhoneNumber;

    @Data
    public static class Location {
        private double lat;
        private double lng;
    }
}
