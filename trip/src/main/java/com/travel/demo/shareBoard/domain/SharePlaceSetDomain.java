package com.travel.demo.shareBoard.domain;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class SharePlaceSetDomain {
    private long plan_id;
    private String id;
    private String displayName;
    private String formattedAddress;
    private String internationalPhoneNumber;
    private double lat;
    private double lng;
    private Date date;
    private int order;
}
