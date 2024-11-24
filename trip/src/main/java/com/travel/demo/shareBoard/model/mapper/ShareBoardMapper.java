package com.travel.demo.shareBoard.model.mapper;

import com.travel.demo.shareBoard.domain.SharePlanDomain;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShareBoardMapper {
    int addSharePlan(SharePlanDomain sharePlan);
}
