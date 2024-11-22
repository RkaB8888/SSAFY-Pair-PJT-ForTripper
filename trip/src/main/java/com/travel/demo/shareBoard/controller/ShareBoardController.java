package com.travel.demo.shareBoard.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/planposts")
@RequiredArgsConstructor
public class ShareBoardController {

    //게시판 전체 조회
    @GetMapping("/")
    public void getPosts() {

    }

    //게시글 작성
    @PostMapping("/add")
    public void addPosts() {
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
