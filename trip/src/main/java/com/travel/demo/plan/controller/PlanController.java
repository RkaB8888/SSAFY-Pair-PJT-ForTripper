package com.travel.demo.plan.controller;

import com.travel.demo.plan.dto.PlanAddRequest;
import com.travel.demo.plan.dto.PlanListResponse;
import com.travel.demo.plan.dto.PlaceListDTO;
import com.travel.demo.plan.model.service.PlanService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/plans")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    //계획 목록 불러오기
    @GetMapping("/")
    public ResponseEntity<?> planList(HttpServletRequest request) {
        System.out.println("planList 호출!");
        String token = request.getHeader("Authorization");
        List<PlanListResponse> list = planService.findListByID(token);
        return ResponseEntity.ok(list);
    }

    //특정 계획 불러오기
    @GetMapping("/{plan_id}")
    public ResponseEntity<?> planDetail(HttpServletRequest request, @PathVariable("plan_id") long plan_id) throws Exception {
        System.out.println("planDetail 호출! plan_id는 " + plan_id);
        String token = request.getHeader("Authorization");
        System.out.println(token);
        PlaceListDTO dto = planService.findVisitPlacesByPlanId(token, plan_id);
        System.out.println("얘 내보낼거임: "  + dto);
        return (ResponseEntity<?>) ResponseEntity.ok(dto);
    }

    //계획 생성
    @PostMapping("/add")
    public String planAdd(HttpServletRequest request, @RequestBody PlanAddRequest planInfo) {
        System.out.println(request.getHeader("Authorization")); //헤더에서 인증정보(토큰)갖고옴
        String token = request.getHeader("Authorization");
        System.out.println("토큰값: " + token);
        planService.PlanAdd(token, planInfo);
        System.out.println(planInfo);
        //토큰 파싱
        //입력된 PlanDto DB에 저장
        return "redirect:/basic/plans/{plan_id}";
    }

    //계획 수정 화면 불러오기
    @GetMapping("/{plan_id}/edit")
    public String editPage(@PathVariable("plan_id") long plan_id, @ModelAttribute PlanAddRequest plan) {
        //DB에 수정 반영
        return "redirect:/basic/plans/{plan_id}";
    }

    //계획 수정
    @PostMapping("/{plan_id}/edit")
    public ResponseEntity<?> edit(@PathVariable("plan_id") long plan_id, @RequestBody PlaceListDTO dailySchedules,
                                  @RequestHeader("Authorization") String token) {
        try {
            System.out.println("스케줄 서버 저장 요청! plan_id는: " + plan_id);
            System.out.println(dailySchedules);
            planService.addVisitPlaces(token, plan_id, dailySchedules);
            // DB에 수정 반영
            // 성공 시 200 OK 반환
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.err.println("에러 발생: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생");
        }
    }
}
