package com.travel.demo.plan.controller;

import com.travel.demo.plan.dto.PlanAddRequest;
import com.travel.demo.plan.dto.PlanListResponse;
import com.travel.demo.plan.dto.VisitPlaceEditRequest;
import com.travel.demo.plan.model.service.PlanService;
import com.travel.demo.users.domain.UserDomain;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
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
    public ResponseEntity<?> planDetail(HttpServletRequest request, @PathVariable long plan_id) {
        System.out.println("planDetail 호출! plan_id는 " + plan_id);
        String token = request.getHeader("Authrization");

        //Authrization의 userID 정보와 plan_id의 plan을 만든 사람의 userID가 일치해야 함
        //일치한다면 해당 plan의 plan과 place 정보 가져와서 리턴
        return (ResponseEntity<?>) ResponseEntity.ok();
    }

    //계획 생성
    @PostMapping("/add")
    public String planAdd(HttpServletRequest request, @RequestBody PlanAddRequest planInfo) {
        System.out.println(request.getHeader("Authorization")); //헤더에서 인증정보(토큰)갖고옴
        String token = request.getHeader("Authorization");
        planService.PlanAdd(token, planInfo);
        System.out.println(planInfo);
        //토큰 파싱
        //입력된 PlanDto DB에 저장
        return "redirect:/basic/plans/{plan_id}";
    }

    //계획 수정 화면 불러오기
    @GetMapping("/{plan_id}/edit")
    public String editPage(@PathVariable long plan_id, @ModelAttribute PlanAddRequest plan) {
        //DB에 수정 반영
        return "redirect:/basic/plans/{plan_id}";
    }

    //계획 수정
    @PostMapping("/{plan_id}/edit")
    public String edit(@PathVariable long plan_id, @ModelAttribute VisitPlaceEditRequest place) {
        //DB에 수정 반영
        return "redirect:/basic/plans/{plan_id}";
    }
}
