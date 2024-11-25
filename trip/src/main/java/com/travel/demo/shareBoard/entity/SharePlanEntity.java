package com.travel.demo.shareBoard.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class SharePlanEntity {
    private long plan_id;
    private long user_id;
    private Timestamp create_date;
    private int total_date;
}
