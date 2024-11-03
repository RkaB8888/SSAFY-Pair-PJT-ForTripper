package com.travel.place.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class PlaceDto {
    private long place_id;
    private int type_id;
    private String title;
    private String add1;
    private String addr2;
    private int areaCode;
    private String cat1;
    private String cat2;
    private String cat3;
    private String firstImage1;
    private String firstImage2;
    private double longitude;
    private double latitude;
    private int mlevel;
    private int area_code;
    private int sigunguCode;
    private String tel;
    private String zipcode;
    private String created_time;
    private String modified_time;
}
