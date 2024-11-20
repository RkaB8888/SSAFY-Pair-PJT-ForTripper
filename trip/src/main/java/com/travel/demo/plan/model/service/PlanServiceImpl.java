package com.travel.demo.plan.model.service;

import com.travel.demo.plan.domain.PlanDomain;
import com.travel.demo.plan.dto.PlaceListResponse;
import com.travel.demo.plan.dto.PlanAddRequest;
import com.travel.demo.plan.dto.PlanListResponse;
import com.travel.demo.plan.entity.PlanEntity;
import com.travel.demo.plan.model.mapper.PlanMapper;
import com.travel.demo.users.model.mapper.AuthMapper;
import com.travel.demo.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService{

    private final JWTUtil jwtUtil;
    private final PlanMapper planMapper;
    private final AuthMapper authMapper;

    @Override
    public int PlanAdd(String token, PlanAddRequest plan) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            String email = jwtUtil.getIdFromToken(token); //token에서 ID(email) 파싱해서 가져옴
            long user_id = authMapper.findByEmail(email).getUser_id();
            PlanDomain domain = new PlanDomain();

            domain.setUser_id(user_id);
            domain.setPlan_title(plan.getPlan_title());
            domain.setStart_date(plan.getStart_date());
            domain.setEnd_date(plan.getEnd_date());
            domain.setDescription(plan.getDescription());
            System.out.println(domain);
            planMapper.planAdd(domain);

        } else {
            throw new IllegalArgumentException("Invalid Authorization header!");
        }
        return 0;
    }

    @Override
    public List<PlanListResponse> findListByID(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            String email = jwtUtil.getIdFromToken(token);
            long user_id = authMapper.findByEmail(email).getUser_id();
            return planMapper.findPlanByID(user_id);
        } else {
            throw new IllegalArgumentException("Invalid Authorization header!");
        }
    }

    @Override
    public List<PlaceListResponse> findVisitPlacesByPlanId(String token, Long planId) {
        if(token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            String email = jwtUtil.getIdFromToken(token);
            long user_id = authMapper.findByEmail(email).getUser_id();
            //1. 참여 멤버 테이블에서 planID로 찾은 데이터 중 userID가 일치하는 데이터가 있는지 확인(참여 멤버인지 확인)
            //2. 일치하지 않으면 오류!
            //3. planID로 일치하는 데이터 모두 찾아 가져옴

            //4. 날짜별로 그룹화해서 리턴
        } else {
            throw new IllegalArgumentException("Invalid Authrization header!");
        }

        return List.of();
    }
}
