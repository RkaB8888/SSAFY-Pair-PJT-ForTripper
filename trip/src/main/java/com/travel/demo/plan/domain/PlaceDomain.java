package com.travel.demo.plan.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
//영업시간 추가해야 함!
@Data
public class PlaceDomain {
    long plan_id;           //계획 ID (FK) Plan dto의 plan_id임
    long user_id;           //작성 회원 ID (FK)
    String visit_google_id; //구글 Place id
    String display_name;    //장소명
    BigDecimal latitude;    //위도
    BigDecimal longitude;   //경도
    String address;         //방문지 주소
    String phone_number;    //방문지 전화번호
    Date visit_date;        //방문 날짜
    int visit_order;        //방문 순서
}
