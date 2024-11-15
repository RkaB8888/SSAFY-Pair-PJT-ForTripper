package com.travel.demo.plan.controller;

import com.travel.demo.plan.dto.PlanDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequestMapping("/basic/plans")
public class PlanController {

    @GetMapping
//    public String planMain(@SessionAttribute(name = "userId", required = false) long userId) {
    public String planMain() {
        //userId가 null(로그인을 하지 않음)일 경우 로그인 페이지로 이동하도록 함

        //userId가 null이 아닐 경우 여행 계획 리스트 띄워 줌
        return "basic/plans";
    }

    @GetMapping("/add")
    public String planAdd() {
        return "basic/plans/add";
    }

    @PostMapping("/add")
    public String planAdd(@ModelAttribute PlanDto planDto, RedirectAttributes redirectAttributes) {
        //DB에 planDto 저장
        //생성된 planDto에서 plan_id 갖고옴
        //redirectAttribute에 데이터 전달

        return "redirect:/basic/plans/{plan_id}";
    }


    //계획 조회
    @GetMapping("/{plan_id}")
    public String plan() {

        return "basic/plan";
    }

    @GetMapping("/{plan_id}/edit")
    public String editPlan(@PathVariable long plan_id, Model model) {
        //plan_id로 DB에서 plan 정보 찾아옴
        //model에 plan을 addAttribute
        return "basic/editForm";
    }

    @PostMapping("/{plan_id}/edit")
    public String edit(@PathVariable long plan_id, @ModelAttribute PlanDto plan) {
        //DB에 수정 반영
        return "redirect:/basic/plans/{plan_id}";
    }
}
