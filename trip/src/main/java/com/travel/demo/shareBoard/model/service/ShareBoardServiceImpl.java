package com.travel.demo.shareBoard.model.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.demo.database.TempFileStorageService;
import com.travel.demo.plan.domain.PlaceDomain;
import com.travel.demo.plan.domain.PlanDomain;
import com.travel.demo.plan.dto.PlaceListDTO;
import com.travel.demo.plan.dto.PlanAddRequest;
import com.travel.demo.plan.model.mapper.PlanMapper;
import com.travel.demo.shareBoard.domain.ShareBoardDomain;
import com.travel.demo.shareBoard.domain.SharePlaceSetDomain;
import com.travel.demo.shareBoard.domain.SharePlanDomain;
import com.travel.demo.shareBoard.dto.ShareAddRequestDTO;
import com.travel.demo.shareBoard.domain.SharePlaceDomain;
import com.travel.demo.shareBoard.dto.ShareBoardResponseDTO;
import com.travel.demo.shareBoard.dto.SharePlaceListDTO;
import com.travel.demo.shareBoard.entity.ShareBoardEntity;
import com.travel.demo.shareBoard.entity.SharePlaceEntity;
import com.travel.demo.shareBoard.entity.SharePlanEntity;
import com.travel.demo.shareBoard.model.mapper.ShareBoardMapper;
import com.travel.demo.users.model.mapper.AuthMapper;
import com.travel.demo.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ShareBoardServiceImpl implements ShareBoardService{

    private final TempFileStorageService tempFileStorageService;
    private final JWTUtil jwtUtil;
    private final AuthMapper authMapper;
    private final ShareBoardMapper shareMapper;
    private final ShareBoardMapper shareBoardMapper;
    private final PlanMapper planMapper;

    @Override
    public int addSharePost(long plan_id, String token, ShareAddRequestDTO requestDTO) throws IOException, ParseException {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            String email = jwtUtil.getIdFromToken(token); //token에서 ID(email) 파싱해서 가져옴
            long user_id = authMapper.findByEmail(email).getUser_id();
            String filename = "";

            if (requestDTO.getImage() != null && !requestDTO.getImage().isEmpty()) {
                filename = tempFileStorageService.saveFile(requestDTO.getImage(), String.valueOf(user_id));
                System.out.println("File saved: " + requestDTO.getImage().getOriginalFilename());
            }

            //SharePlan 세팅 후 DB 저장
            SharePlanDomain sharePlanDomain = new SharePlanDomain();
            sharePlanDomain.setPlan_id(plan_id);
            sharePlanDomain.setTotal_date(requestDTO.getTotalDate());
            sharePlanDomain.setUser_id(user_id);

            System.out.println(sharePlanDomain);
            shareMapper.addSharePlan(sharePlanDomain);

            //ShareBoard 세팅 후 DB 저장
            ShareBoardDomain shareBoardDomain = new ShareBoardDomain();
            shareBoardDomain.setPlan_id(plan_id);
            shareBoardDomain.setUser_id(user_id);
            shareBoardDomain.setTitle(requestDTO.getTitle());
            shareBoardDomain.setContent(requestDTO.getContent());
            if(!filename.isEmpty()) shareBoardDomain.setImage_name(filename);

            System.out.println(shareBoardDomain);
            shareMapper.addSharePost(shareBoardDomain);

            //place 세팅 후 DB 저장
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, List<SharePlaceDomain>> dailySchedules = objectMapper.readValue(requestDTO.getDailySchedules(),
                    new TypeReference<Map<String, List<SharePlaceDomain>>>() {});

            for (String s : dailySchedules.keySet()) {
                List<SharePlaceDomain> list = dailySchedules.get(s);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date visitedDate = formatter.parse(s);
                int order = 1;
                for (SharePlaceDomain sharePlaceDomain : list) {
                    SharePlaceSetDomain domain = new SharePlaceSetDomain();
                    domain.setPlan_id(plan_id);
                    domain.setId(sharePlaceDomain.getId());
                    domain.setDisplayName(sharePlaceDomain.getDisplayName());
                    domain.setFormattedAddress(sharePlaceDomain.getFormattedAddress());
                    domain.setInternationalPhoneNumber(sharePlaceDomain.getInternationalPhoneNumber());
                    domain.setLat(sharePlaceDomain.getLocation().getLat());
                    domain.setLng(sharePlaceDomain.getLocation().getLng());
                    domain.setDate(visitedDate);
                    domain.setOrder(order);
                    System.out.println(domain);
                    shareBoardMapper.addSharePlanPlace(domain);
                    order++;
                }
            }
        } else {
            throw new IllegalArgumentException("Invalid Authorization header!");
        }
        return 0;
    }

    @Override
    public List<ShareBoardResponseDTO> findShareBoardList() throws IOException {
        List<ShareBoardEntity> entityList = shareBoardMapper.findShareBoardAll();
        List<ShareBoardResponseDTO> responseList = new ArrayList<>();
        for (ShareBoardEntity shareBoardEntity : entityList) {
            ShareBoardResponseDTO dto = new ShareBoardResponseDTO();
            dto.setPost_id(shareBoardEntity.getPost_id());
            dto.setUser_id(shareBoardEntity.getUser_id());
            dto.setPlan_id(shareBoardEntity.getPlan_id());
            dto.setTitle(shareBoardEntity.getTitle());
            dto.setContent(shareBoardEntity.getContent());
            dto.setCreated_time(shareBoardEntity.getCreated_time());
            String imageUrl = "http://localhost:9000/trip/images/" + shareBoardEntity.getImage_name();
            dto.setImageUrl(imageUrl);
            responseList.add(dto);
        }
        return responseList;
    }

    @Override
    public SharePlaceListDTO findVisitPlacesByPlanId(Long planId) {
        System.out.println("share plan place 찾기!");
        List<SharePlaceEntity> list = shareMapper.findSharePlacesByPlanId(planId);
        SharePlaceListDTO placeListDTO = new SharePlaceListDTO();
        Map<String, List<SharePlaceListDTO.PlaceDTO>> dailySchedules = placeListDTO.getDailySchedules();

        for (SharePlaceEntity sharePlaceEntity : list) {
            SharePlaceListDTO.PlaceDTO cur = new SharePlaceListDTO.PlaceDTO();
            cur.setId(sharePlaceEntity.getVisit_google_id());
            cur.setDisplayName(sharePlaceEntity.getDisplay_name());
            cur.setLocation(new SharePlaceListDTO.Location(sharePlaceEntity.getLatitude(), sharePlaceEntity.getLongitude()));
            cur.setFormattedAddress(sharePlaceEntity.getAddress());
            cur.setInternationalPhoneNumber(sharePlaceEntity.getPhone_number());
            cur.setVisit_order(sharePlaceEntity.getVisit_order());

            dailySchedules
                    .computeIfAbsent(sharePlaceEntity.getVisit_date().toString(), k -> new ArrayList<>())
                    .add(cur);
        }

        placeListDTO.setDailySchedules(dailySchedules);
        return placeListDTO;
    }

    @Override
    public void copyAddPlan(String token, Long planId, PlanAddRequest planAddRequest) throws ParseException {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            String email = jwtUtil.getIdFromToken(token);
            long user_id = authMapper.findByEmail(email).getUser_id();
            PlanDomain domain = new PlanDomain();

            //Plan 생성
            domain.setUser_id(user_id);
            domain.setPlan_title(planAddRequest.getPlan_title());
            domain.setStart_date(planAddRequest.getStart_date());
            domain.setEnd_date(planAddRequest.getEnd_date());
            domain.setDescription(planAddRequest.getDescription());
            planMapper.planAdd(domain);

            //생성된 Plan 아이디 가져오기
            long newPlanID = shareMapper.findPlanIdRecentByUserId(user_id);

            //여행 첫 번째 날짜 찾기
            LocalDate firstDate = shareMapper.findFirstDateByPlanId(planId);
            LocalDate startDate = planAddRequest.getStart_date();

            //두 날짜 사이의 날짜 차이 계산
            long daysBetween = ChronoUnit.DAYS.between(firstDate, startDate);

            SharePlaceListDTO placeListDTO = findVisitPlacesByPlanId(planId);
            Map<String, List<SharePlaceListDTO.PlaceDTO>> schedules = placeListDTO.getDailySchedules();
            for (String s : schedules.keySet()) {
                List<SharePlaceListDTO.PlaceDTO> list = schedules.get(s);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date visitedDate = formatter.parse(s);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(visitedDate);
                calendar.add(Calendar.DATE, (int) daysBetween);
                visitedDate = calendar.getTime();
                for (SharePlaceListDTO.PlaceDTO placeDTO : list) {
                    PlaceDomain place = new PlaceDomain();
                    place.setPlan_id(newPlanID);
                    place.setUser_id(user_id);
                    place.setVisit_google_id(placeDTO.getId());
                    place.setDisplay_name(placeDTO.getDisplayName());
                    place.setLatitude(placeDTO.getLocation().getLat());
                    place.setLongitude(placeDTO.getLocation().getLng());
                    place.setAddress(placeDTO.getFormattedAddress());
                    place.setPhone_number(placeDTO.getInternationalPhoneNumber());
                    place.setVisit_date(visitedDate);
                    place.setVisit_order(placeDTO.getVisit_order());
                    System.out.println("얘 저장할거임 plan_id는: " +newPlanID);
                    System.out.println(place);
                    planMapper.addPlace(place);
                }
            }

        } else {
            throw new IllegalArgumentException("Invalid Authorization header!");
        }
    }
}
