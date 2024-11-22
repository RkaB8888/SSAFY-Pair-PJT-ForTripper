package com.travel.demo.plan.domain;

import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDate;

@Data
public class PlanDomain {
    private long user_id;
    private String plan_title;
    private LocalDate start_date;
    private LocalDate end_date;
    private int total_date;
    private String description;
}

