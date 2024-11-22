package com.travel.demo.plan.model.service;

import com.travel.demo.plan.dto.PlaceListDTO;
import com.travel.demo.plan.dto.PlanAddRequest;
import com.travel.demo.plan.dto.PlanListResponse;
import org.springframework.web.bind.annotation.RequestBody;

import java.text.ParseException;
import java.util.List;

public interface PlanService {
    int PlanAdd(String token, PlanAddRequest plan);
    List<PlanListResponse> findListByID(String token);
    PlaceListDTO findVisitPlacesByPlanId(String token, Long planId) throws Exception;
    int addVisitPlaces(String token, Long planId, @RequestBody PlaceListDTO dailySchedules) throws ParseException;
}
