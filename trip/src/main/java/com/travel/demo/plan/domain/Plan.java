package com.travel.demo.plan.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class Plan {
    private long plan_id;
    private long user_id;
    private String plan_title;
    private Timestamp startDate;
    private Timestamp endDate;
    private int total_date;
    private String description;
}
