package com.travel.demo.plan.model.service;

import com.travel.demo.plan.domain.PlaceDomain;
import com.travel.demo.plan.domain.PlanDomain;
import com.travel.demo.plan.dto.PlaceListDTO;
import com.travel.demo.plan.dto.PlanAddRequest;
import com.travel.demo.plan.dto.PlanListResponse;
import com.travel.demo.plan.entity.PlaceEntity;
import com.travel.demo.plan.model.mapper.PlanMapper;
import com.travel.demo.users.model.mapper.AuthMapper;
import com.travel.demo.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    public PlaceListDTO findVisitPlacesByPlanId(String token, Long planId) throws Exception {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            String email = jwtUtil.getIdFromToken(token);
            long user_id = authMapper.findByEmail(email).getUser_id();
            long find_id = planMapper.findUserIdByPlanID(planId);

            if (user_id == find_id) {
                List<PlaceEntity> list = planMapper.findVisitPlacesByPlanId(planId);
                PlaceListDTO placeListDTO = new PlaceListDTO();
                Map<String, List<PlaceListDTO.PlaceDTO>> dailySchedules = placeListDTO.getDailySchedules(); // getter로 Map을 가져옴

                for (PlaceEntity placeEntity : list) {
                    PlaceListDTO.PlaceDTO cur = new PlaceListDTO.PlaceDTO();
                    cur.setId(placeEntity.getVisit_google_id());
                    cur.setDisplayName(placeEntity.getDisplay_name());
                    cur.setLocation(new PlaceListDTO.Location(placeEntity.getLatitude(), placeEntity.getLongitude()));
                    cur.setFormattedAddress(placeEntity.getAddress());
                    cur.setInternationalPhoneNumber(placeEntity.getPhone_number());

                    dailySchedules
                            .computeIfAbsent(placeEntity.getVisit_date().toString(), k -> new ArrayList<>())
                            .add(cur);
                }

                placeListDTO.setDailySchedules(dailySchedules); // dailySchedules을 PlaceListDTO에 설정

                return placeListDTO;
            } else {
                throw new Exception("권한이 없습니다!");
            }
        } else {
            throw new IllegalArgumentException("Invalid Authorization header!");
        }
    }


    @Override
    public int addVisitPlaces(String token, Long planId, PlaceListDTO dailySchedules) throws ParseException {
        if(token != null && token.startsWith("Bearer ")) {
            //1. plan_id로 plan 만든 사람 id 가져와서 token의 id와 비교하여 동일한지 확인
            token = token.substring(7);
            String email = jwtUtil.getIdFromToken(token);
            long user_id = authMapper.findByEmail(email).getUser_id();
            if(planMapper.findUserIdByPlanID(planId) == user_id) {
                planMapper.deletePlaceAllByPlanID(planId);
                Map<String, List<PlaceListDTO.PlaceDTO>> schedules = dailySchedules.getDailySchedules();
                for (String s : schedules.keySet()) {
                    List<PlaceListDTO.PlaceDTO> list = schedules.get(s);
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    Date visitedDate = formatter.parse(s);
                    int order = 1;
                    for (PlaceListDTO.PlaceDTO placeDTO : list) {
                        PlaceDomain place = new PlaceDomain();
                        place.setPlan_id(planId);
                        place.setUser_id(user_id);
                        place.setVisit_google_id(placeDTO.getId());
                        place.setDisplay_name(placeDTO.getDisplayName());
                        place.setLatitude(placeDTO.getLocation().getLat());
                        place.setLongitude(placeDTO.getLocation().getLng());
                        place.setAddress(placeDTO.getFormattedAddress());
                        place.setPhone_number(placeDTO.getInternationalPhoneNumber());
                        place.setVisit_date(visitedDate);
                        place.setVisit_order(order);
                        System.out.println(place);
                        planMapper.addPlace(place);
                        order++;
                    }
                }
            }
            //2. 동일하지 않으면 plan이 공유되어 있는지 확인, 공유되어 있지 않으면 오류, 공유되어 있으면 해당 plan에 참여하고 있는 사람인지 확인 (이건 나중에)
            //3. plan_id로 visited table의 데이터 삭제 한 후 클라이언트에서 보낸 dailySchedules 데이터들 차례대로 저장함
        } else {
            throw new IllegalArgumentException("Invalid Authorization header! ");
        }
        return 0;
    }
}

