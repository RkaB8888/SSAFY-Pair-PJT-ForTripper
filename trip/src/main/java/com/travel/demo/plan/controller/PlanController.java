package com.travel.demo.plan.controller;

import com.travel.demo.plan.dto.PlanAddRequest;
import com.travel.demo.plan.dto.VisitPlaceEditRequest;
import com.travel.demo.users.domain.UserDomain;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@RestController
@RequestMapping("/plans")
public class PlanController {

    //계획 생성
    @PostMapping("/add")
    public String planAdd(HttpServletRequest request) {
        //DB에 planDto 저장
        //생성된 planDto에서 plan_id 갖고옴
        //redirectAttribute에 데이터 전달
        System.out.println(request.getHeader("Authorization")); //헤더에서 인증정보(토큰)갖고옴
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
