package com.travel.demo.shareBoard.controller;

import com.travel.demo.plan.dto.PlanAddRequest;
import com.travel.demo.shareBoard.dto.ShareAddRequestDTO;
import com.travel.demo.shareBoard.dto.ShareBoardResponseDTO;
import com.travel.demo.shareBoard.dto.SharePlaceListDTO;
import com.travel.demo.shareBoard.model.service.ShareBoardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/planposts")
@RequiredArgsConstructor
public class ShareBoardController {

    private final ShareBoardService shareBoardService;

    //게시판 전체 조회
    @GetMapping("/")
    public ResponseEntity<?> getPosts() throws IOException {
        List<ShareBoardResponseDTO> list = shareBoardService.findShareBoardList();
        return ResponseEntity.ok(list);
    }

    //게시글 작성
    @PostMapping(value = "/add/{plan_id}", consumes = "multipart/form-data")
    public ResponseEntity<?> addSharePlan(@PathVariable("plan_id") long plan_id, @ModelAttribute ShareAddRequestDTO requestDTO, HttpServletRequest request) {
        try {
            System.out.println(request.getHeader("Authorization")); //헤더에서 인증정보(토큰)갖고옴
            String token = request.getHeader("Authorization");
            System.out.println("공유되는 plan_id: " + plan_id);

            shareBoardService.addSharePost(plan_id, token, requestDTO);
            System.out.println("저장됐나?");
            return ResponseEntity.ok("Plan added successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error processing request: " + e.getMessage());
        }
    }

    //게시글 조회
    @GetMapping("/{plan_id}")
    public ResponseEntity<?> getPost(@PathVariable("plan_id") long plan_id) {
        SharePlaceListDTO placeListDTO = shareBoardService.findVisitPlacesByPlanId(plan_id);
        return ResponseEntity.ok(placeListDTO);
    }

    //게시글 삭제
    @DeleteMapping("/{post_id}")
    public void deletePost() {

    }

    //게시글 수정
    @PutMapping("/edit/{post_id}")
    public void editPost() {

    }

    //게시된 플랜 내 플랜으로 저장
    @PostMapping("/share/{plan_id}")
    public String copyAddPlan(@PathVariable("plan_id") long plan_id, @RequestBody PlanAddRequest plan,
                                      @RequestHeader("Authorization") String token) throws ParseException {
        shareBoardService.copyAddPlan(token, plan_id, plan);
        return "redirect:/basic/plans/{plan_id}";
    }
}
