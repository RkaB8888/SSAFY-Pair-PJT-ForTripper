package com.travel.demo.plan.model.mapper;

import com.travel.demo.plan.domain.PlaceDomain;
import com.travel.demo.plan.domain.PlanDomain;
import com.travel.demo.plan.dto.PlanListResponse;
import com.travel.demo.plan.entity.PlaceEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PlanMapper {
    int planAdd(PlanDomain plan);
    List<PlanListResponse> findPlanByID(long userId);
    List<PlaceEntity> findVisitPlacesByPlanId(Long plan_id);
    long findUserIdByPlanID(Long plan_id);
    int deletePlaceAllByPlanID(Long plan_id);
    int addPlace(PlaceDomain place);
}
