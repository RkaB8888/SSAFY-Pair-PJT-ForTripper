package com.travel.demo.users.controller;

import com.travel.demo.users.domain.UserDomain;
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
    	UserDomain user = authService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(Map.of("message", "사용자를 찾을 수 없습니다.", "status", 404));
        }
        return ResponseEntity.ok(Map.of("email",user.getEmail(),"name",user.getName(),"nickname",user.getNickName(),"role",user.getRole(), "status", 200));
    }
    @GetMapping("/nickname/{nickname}")
    public ResponseEntity<?> findByNickName(@PathVariable("nickname") String nickname) {
    	System.out.println("전달 받은 닉네임 : "+nickname);
    	UserDomain user = authService.findByNickName(nickname);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(Map.of("message", "사용자를 찾을 수 없습니다.", "status", 404));
        }
        return ResponseEntity.ok(Map.of("message", "중복된 닉네임입니다.", "status", 200));
    }
    @GetMapping("/validate")
    public ResponseEntity<?> validateAccessToken(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        System.out.println("토큰 검사 요청 받음");
        System.out.println("AccessToken: " + authorizationHeader);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
        }

        String token = authorizationHeader.substring(7); // "Bearer " 제거

        if (token.equals("null") || !authService.isValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
        }

        return ResponseEntity.ok("Valid Token");
    }
    @GetMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam("token") String token) {
        boolean isVerified = authService.verifyEmail(token);
        if (isVerified) {
            return ResponseEntity.ok(Map.of("message", "이메일 인증이 완료되었습니다. 이제 로그인할 수 있습니다."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "유효하지 않거나 만료된 인증 링크입니다."));
        }
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
        Map<String, String> tokens = authService.login(loginInfo);
        if (tokens == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(Map.of("message", "아이디 또는 비밀번호가 잘못되었거나 이메일 인증이 필요합니다."));
        }
        return ResponseEntity.ok(tokens);
    }
    @PostMapping("/refresh")
    public ResponseEntity<?> validateRefreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        System.out.println("토큰 검사 요청 받음");
    	System.out.println("RefreshToken: "+refreshToken);
        if (refreshToken == null || refreshToken.isEmpty()) {
        	System.out.println("POST 토큰 검사 오류");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Refresh token is missing");
        }

        // Refresh Token 검증
        if (!authService.isValid(refreshToken)) {
        	System.out.println("POST 토큰 만료");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Refresh Token");
        }

        // 유효한 Refresh Token이면 새로운 Access Token 발급
        String newAccessToken = authService.generateNewAccessToken(refreshToken);

        if (newAccessToken == null) {
        	System.out.println("새로운 액세스 토큰 발급 오류");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to generate Access Token");
        }

        // 새 Access Token 반환
        return ResponseEntity.ok(Map.of("access-token", newAccessToken));
    }
    
    //delete
}
