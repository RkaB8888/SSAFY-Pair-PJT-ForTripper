package com.travel.demo.users.controller;

import com.travel.demo.users.dto.FindUserResponse;
import com.travel.demo.users.dto.UserLoginRequest;
import com.travel.demo.users.dto.UserSignUpRequest;
import com.travel.demo.users.model.service.AuthService;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class MemberController {
    private final AuthService authService;

    //get
    @GetMapping("/email/{email}")
    public ResponseEntity<?> findById(@PathVariable("email") String email) {
        FindUserResponse user = authService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(Map.of("message", "사용자를 찾을 수 없습니다.", "status", 404));
        }
        return ResponseEntity.ok(Map.of("message", "중복된 이메일입니다.", "status", 200));
    }
  //get
    @GetMapping("/nickname/{nickname}")
    public ResponseEntity<?> findByNickName(@PathVariable("nickname") String nickname) {
    	System.out.println("전달 받은 닉네임 : "+nickname);
        FindUserResponse user = authService.findByNickName(nickname);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(Map.of("message", "사용자를 찾을 수 없습니다.", "status", 404));
        }
        return ResponseEntity.ok(Map.of("message", "중복된 닉네임입니다.", "status", 200));
    }
    
    //post
    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody UserSignUpRequest joinInfo) {
        String token = authService.join(joinInfo);
        if (token == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원가입 실패");
        return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest loginInfo) {
        System.out.println(loginInfo);
        String token = authService.login(loginInfo);
        if(token == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("아이디 또는 비밀번호가 잘못되었습니다.");
        return ResponseEntity.ok(token);
    }
    
    //delete
}
