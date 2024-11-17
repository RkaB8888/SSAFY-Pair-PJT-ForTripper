package com.travel.demo.users.controller;

import com.travel.demo.users.dto.UserLoginRequest;
import com.travel.demo.users.dto.UserSignUpRequest;
import com.travel.demo.users.model.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class MemberController {
    private final AuthService authService;

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody UserSignUpRequest joinInfo) {
        String token = authService.join(joinInfo);
        if (token == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원가입 실패");
        return ResponseEntity.ok(token);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest loginInfo) {
        System.out.println(loginInfo);
        String token = authService.login(loginInfo);
        if(token == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("아이디 또는 비밀번호가 잘못되었습니다.");
        return ResponseEntity.ok(token);
    }
}
