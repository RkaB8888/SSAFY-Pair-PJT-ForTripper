package com.travel.demo.plan.model.service;

import com.travel.demo.plan.domain.PlanDomain;
import com.travel.demo.plan.dto.PlanAddRequest;
import com.travel.demo.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService{

    private final JWTUtil jwtUtil;

    @Override
    public int PlanAdd(String token, PlanAddRequest plan) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            String id = jwtUtil.getIdFromToken(token); //token에서 userID 가져옴
            PlanDomain domain = new PlanDomain();
//            private long user_id;
//            private String plan_title;
//            private Timestamp startDate;
//            private Timestamp endDate;
//            private int total_date;
//            private String description;

//            domain.setUser_id();
            domain.setPlan_title(plan.getPlan_title());
            domain.setStartDate(plan.getStartDate());
            domain.setEndDate(plan.getEndDate());
//            domain.getTotal_date()
            domain.setDescription(plan.getDescription());
        } else {
            throw new IllegalArgumentException("Invalid Authorization header!");
        }
        return 0;
    }
}
