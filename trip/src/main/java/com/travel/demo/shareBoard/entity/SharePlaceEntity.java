package com.travel.demo.shareBoard.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SharePlaceEntity {
    private String visit_google_id;
    private String display_name;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String address;
    private String phone_number;
    private LocalDate visit_date;
    private int visit_order;
}
