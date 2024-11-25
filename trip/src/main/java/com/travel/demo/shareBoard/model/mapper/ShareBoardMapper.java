package com.travel.demo.shareBoard.model.mapper;

import com.travel.demo.shareBoard.domain.ShareBoardDomain;
import com.travel.demo.shareBoard.domain.SharePlaceSetDomain;
import com.travel.demo.shareBoard.domain.SharePlanDomain;
import com.travel.demo.shareBoard.entity.ShareBoardEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ShareBoardMapper {
    void addSharePost(ShareBoardDomain sharePost);
    void addSharePlan(SharePlanDomain sharePlan);
    void addSharePlanPlace(SharePlaceSetDomain sharePlace);
    List<ShareBoardEntity> findShareBoardAll();
}
