package com.travel.demo.shareBoard.controller;

import com.travel.demo.shareBoard.dto.ShareAddRequestDTO;
import com.travel.demo.shareBoard.model.service.ShareBoardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/planposts")
@RequiredArgsConstructor
public class ShareBoardController {

    private final ShareBoardService shareBoardService;

    //게시판 전체 조회
    @GetMapping("/")
    public void getPosts() {

    }

    //게시글 작성
    @PostMapping(value = "/add", consumes = "multipart/form-data")
    public ResponseEntity<?> addSharePlan(@ModelAttribute ShareAddRequestDTO requestDTO, HttpServletRequest request) {
        try {
            System.out.println(request.getHeader("Authorization")); //헤더에서 인증정보(토큰)갖고옴
            String token = request.getHeader("Authorization");
//            System.out.println("토큰값: " + token);
//            System.out.println("Title: " + requestDTO.getTitle());
//            System.out.println("Content: " + requestDTO.getContent());
//            System.out.println("Daily Schedules: " + requestDTO.getDailySchedules());
//            System.out.println("Total Date: " + requestDTO.getTotalDate());
//
//            if (requestDTO.getImage() != null) {
//                System.out.println("Image: " + requestDTO.getImage().getOriginalFilename());
//            }
//
//            ObjectMapper objectMapper = new ObjectMapper();
//            Map<String, List<SharePlace>> dailySchedules = objectMapper.readValue(requestDTO.getDailySchedules(),
//                    new TypeReference<Map<String, List<SharePlace>>>() {});
//
//            for (String s : dailySchedules.keySet()) {
//                System.out.println(s);
//                System.out.println(dailySchedules.get(s));
//            }

            shareBoardService.addSharePost(token, requestDTO);

            return ResponseEntity.ok("Plan added successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error processing request: " + e.getMessage());
        }
    }

    //게시글 조회
    @GetMapping("/{post_id}")
    public void getPost() {

    }

    //게시글 삭제
    @DeleteMapping("/{post_id}")
    public void deletePost() {

    }

    //게시글 수정
    @PutMapping("/edit/{post_id}")
    public void editPost() {

    }
}
