package com.travel.demo.plan.model.service;

import com.travel.demo.plan.dto.PlaceListResponse;
import com.travel.demo.plan.dto.PlanAddRequest;
import com.travel.demo.plan.dto.PlanListResponse;

import java.util.List;

public interface PlanService {
    int PlanAdd(String token, PlanAddRequest plan);
    List<PlanListResponse> findListByID(String token);
    List<PlaceListResponse> findVisitPlacesByPlanId(String token, Long planId);
}
