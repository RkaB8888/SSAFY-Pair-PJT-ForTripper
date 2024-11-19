package com.travel.demo.plan.model.mapper;

import com.travel.demo.plan.domain.PlanDomain;
import com.travel.demo.plan.dto.PlanListResponse;
import com.travel.demo.plan.entity.PlanEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PlanMapper {
    int planAdd(PlanDomain plan);
    List<PlanListResponse> findPlanByID(long userId);
}
