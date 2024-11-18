package com.travel.demo.plan.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class PlanEntity {
    private long plan_id;
    private long user_id; //생성한 사람 id
    private String plan_title;
    private Timestamp startDate;
    private Timestamp endDate;
    private int total_date;
    private String description;
}
