package com.travel.demo.plan.model.service;

import com.travel.demo.plan.dto.PlanAddRequest;

public interface PlanService {
    int PlanAdd(String token, PlanAddRequest plan);
}
