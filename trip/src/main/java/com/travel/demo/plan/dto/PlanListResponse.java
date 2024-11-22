package com.travel.demo.plan.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PlanListResponse {
    private long plan_id; //계획 아이디
    private String plan_title; //계획 이름
    private LocalDate start_date; //여행 시작 날짜
    private LocalDate end_date;   //여행 마지막 날짜
    private int total_date;      //여행 기간(일)
    private String description;  //여행 설명
}
