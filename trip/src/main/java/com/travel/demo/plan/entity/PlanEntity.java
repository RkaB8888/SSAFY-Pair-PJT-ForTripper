package com.travel.demo.plan.entity;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PlanEntity {
    private long plan_id; //여행 계획 id
    private long user_id; //생성한 사람 id
    private String plan_title; //계획 이름
    private LocalDate start_date; //여행 시작 날짜
    private LocalDate end_date;   //여행 마지막 날짜
    private int total_date;      //여행 기간(일)
    private String description;  //여행 설명
}
