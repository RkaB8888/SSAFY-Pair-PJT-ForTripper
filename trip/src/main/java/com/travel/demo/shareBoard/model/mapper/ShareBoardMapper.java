package com.travel.demo.shareBoard.model.mapper;

import com.travel.demo.shareBoard.domain.ShareBoardDomain;
import com.travel.demo.shareBoard.domain.SharePlaceSetDomain;
import com.travel.demo.shareBoard.domain.SharePlanDomain;
import com.travel.demo.shareBoard.entity.ShareBoardEntity;
import com.travel.demo.shareBoard.entity.SharePlaceEntity;
import com.travel.demo.shareBoard.entity.SharePlanEntity;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Mapper
public interface ShareBoardMapper {
    void addSharePost(ShareBoardDomain sharePost);
    void addSharePlan(SharePlanDomain sharePlan);
    void addSharePlanPlace(SharePlaceSetDomain sharePlace);
    List<ShareBoardEntity> findShareBoardAll();
    List<SharePlaceEntity> findSharePlacesByPlanId(Long planId);
    SharePlanEntity findPlanByPlanId(Long planId);
    LocalDate findFirstDateByPlanId(Long planId);
    long findPlanIdRecentByUserId(Long user_id);
}
