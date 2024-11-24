package com.travel.demo.shareBoard.domain;

import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDate;

@Data
public class SharePlanDomain {
    private long plan_id;
    private long user_id;
    private int total_date;
}
