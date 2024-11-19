package com.travel.demo.plan.model.service;

import com.travel.demo.plan.dto.PlanAddRequest;
import com.travel.demo.plan.dto.PlanListResponse;
import com.travel.demo.plan.entity.PlanEntity;

import java.util.List;

public interface PlanService {
    int PlanAdd(String token, PlanAddRequest plan);
    List<PlanListResponse> findListByID(String token);
}
