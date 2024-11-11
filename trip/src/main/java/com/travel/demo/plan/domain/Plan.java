package com.travel.demo.plan.domain;

import lombok.Data;

//영업시간 추가해야 함!
@Data
public class Plan {
    long plan_id;            //계획 ID (PK)
    long user_id;           //작성 회원 ID
    String plan_title;      //계획 타이틀명
    String create_date;     //생성일
    String start_date;      //여행 시작일
    String end_date;        //여행 종료일
    int total_date;         //여행 지속일
    String description;     //여행 설명
}
