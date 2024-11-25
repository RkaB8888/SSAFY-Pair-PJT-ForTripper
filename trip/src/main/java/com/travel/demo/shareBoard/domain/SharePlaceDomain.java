package com.travel.demo.shareBoard.domain;

import lombok.Data;

@Data
public class SharePlaceDomain {
    private long plan_id;
    private long user_id;
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
