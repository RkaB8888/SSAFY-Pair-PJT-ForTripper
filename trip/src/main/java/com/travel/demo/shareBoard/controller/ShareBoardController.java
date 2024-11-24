package com.travel.demo.shareBoard.controller;

import com.travel.demo.database.TempFileStorageService;
import com.travel.demo.shareBoard.dto.ShareAddRequestDTO;
import com.travel.demo.users.dto.SharePlace;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/planposts")
@RequiredArgsConstructor
public class ShareBoardController {
    @Autowired
    private TempFileStorageService tempFileStorageService;

    //게시판 전체 조회
    @GetMapping("/")
    public void getPosts() {

    }

    //게시글 작성
    @PostMapping(value = "/add", consumes = "multipart/form-data")
    public ResponseEntity<?> addSharePlan(@ModelAttribute ShareAddRequestDTO requestDTO) {
        try {
            System.out.println("Title: " + requestDTO.getTitle());
            System.out.println("Content: " + requestDTO.getContent());
            System.out.println("Daily Schedules: " + requestDTO.getDailySchedules());
            System.out.println("Total Date: " + requestDTO.getTotalDate());

            if (requestDTO.getImage() != null) {
                System.out.println("Image: " + requestDTO.getImage().getOriginalFilename());
            }

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, List<SharePlace>> dailySchedules = objectMapper.readValue(requestDTO.getDailySchedules(),
                    new TypeReference<Map<String, List<SharePlace>>>() {});

            for (String s : dailySchedules.keySet()) {
                System.out.println(s);
                System.out.println(dailySchedules.get(s));
            }

            if (requestDTO.getImage() != null && !requestDTO.getImage().isEmpty()) {
                tempFileStorageService.saveFile(requestDTO.getImage());
                System.out.println("File saved: " + requestDTO.getImage().getOriginalFilename());
            }


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
