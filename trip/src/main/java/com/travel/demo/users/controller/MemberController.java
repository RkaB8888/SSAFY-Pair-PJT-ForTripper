package com.travel.demo.users.controller;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.travel.demo.users.domain.UserDomain;
import com.travel.demo.users.dto.UserLoginRequest;
import com.travel.demo.users.dto.UserSignUpRequest;
import com.travel.demo.users.model.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class MemberController {
    private final AuthService authService;
    

    @Value("${app.frontend-url}")
    private String frontendUrl;

    //get
    @GetMapping("/email/{email}")
    public ResponseEntity<?> findById(@PathVariable("email") String email) {
    	UserDomain user = authService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(Map.of("message", "사용자를 찾을 수 없습니다.", "status", 404));
        }
        String profileImage = user.getProfileImage() != null ? user.getProfileImage() : "null";
        return ResponseEntity.ok(Map.of("email",user.getEmail(),"name",user.getName(),"nickname",user.getNickName(),"role",user.getRole(),"joinDate",user.getJoinDate(),"profileImage",profileImage, "status", 200));
    }
    @GetMapping("/nickname/{nickname}")
    public ResponseEntity<?> findByNickName(@PathVariable("nickname") String nickname) {
    	System.out.println("전달 받은 닉네임 : "+nickname);
    	UserDomain user = authService.findByNickName(nickname);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(Map.of("message", "사용자를 찾을 수 없습니다.", "status", 404));
        }
        String profileImage = user.getProfileImage() != null ? user.getProfileImage() : "null";
        return ResponseEntity.ok(Map.of("email",user.getEmail(),"name",user.getName(),"nickname",user.getNickName(),"role",user.getRole(),"joinDate",user.getJoinDate(),"profileImage",profileImage, "status", 200));
    }
    @GetMapping("/validate")
    public ResponseEntity<?> validateAccessToken(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        System.out.println("토큰 검사 요청 받음");
        System.out.println("AccessToken: " + authorizationHeader);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
        }

        String token = authService.getToken(authorizationHeader);

        if (token.equals("null") || !authService.isValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
        }

        return ResponseEntity.ok("Valid Token");
    }
    @GetMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam("token") String token) {
    	System.out.println("인증 요청 받음");
        boolean isVerified = authService.verifyEmail(token);
        if (isVerified) {
        	String asciiArt = "<div style=\"text-align: center; margin-top: 50px;\">\n" +
                    "<pre style=\"font-family: monospace; font-size: 24px;\">\n" +
                    " (\\_/)\n" +
                    " ( •_•)\n" +
                    " / >❤️\n" +
                    "</pre>\n" +
                    "<p style=\"font-size: 18px;\">이메일 인증이 완료되었습니다. 이제 로그인할 수 있습니다.</p>\n" +
                    "<a href=\"" + frontendUrl + "/auth/login\" style=\"font-size: 18px;\">로그인 하러 가기</a>\n" +
                    "</div>";
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE)
                    .body(asciiArt);
        } else {
        	String errorPage = "<div style=\"text-align: center; margin-top: 50px;\">\n" +
                    "<pre style=\"font-family: monospace; font-size: 24px; color: red;\">\n" +
                    " (x_x)\n" +
                    "</pre>\n" +
                    "<h2>유효하지 않거나 만료된 인증 링크입니다.</h2>\n" +
                    "<a href=\"" + frontendUrl + "/auth/resend\" style=\"font-size: 18px;\">인증 이메일 다시 받기</a>\n" +
                    "</div>";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE)
                    .body(errorPage);
        }
    }
    @GetMapping("/profileIMG/{filename}")
    @ResponseBody
    public ResponseEntity<?> getFile(@PathVariable String filename) {
        try {
            // 파일 경로 설정
            Path filePath = Paths.get("C:/Board", filename);

            // 파일 존재 여부 확인
            if (!Files.exists(filePath)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("파일이 존재하지 않습니다.");
            }

            // 파일 리소스 로드
            Resource resource = new FileSystemResource(filePath);

            // MIME 타입 동적 결정
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            // 파일 응답 반환
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("파일을 로드하는 중 오류가 발생했습니다.");
        }
    }
    @GetMapping("/search")
    public ResponseEntity<?> searchUsers(@RequestParam("nickname") String nickname) {
        List<UserDomain> users = authService.searchUsersByNickname(nickname);
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(Map.of("message", "사용자를 찾을 수 없습니다."));
        }
        return ResponseEntity.ok(users);
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
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        boolean isSuccess = authService.createPasswordResetToken(email);
        if (isSuccess) {
            return ResponseEntity.ok(Map.of("message", "비밀번호 재설정 이메일을 전송했습니다."));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(Map.of("message", "해당 이메일의 사용자를 찾을 수 없습니다."));
        }
    }
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");

        boolean isSuccess = authService.resetPassword(token, newPassword);
        if (isSuccess) {
            return ResponseEntity.ok(Map.of("message", "비밀번호가 성공적으로 재설정되었습니다."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(Map.of("message", "유효하지 않거나 만료된 토큰입니다."));
        }
    }
    @PostMapping("/profile-image")
    public ResponseEntity<?> updateProfileImage(
        @RequestParam("profileImage") MultipartFile profileImage,
        @RequestHeader("Authorization") String authorizationHeader) {

        String email = authService.extractEmailFromToken(authorizationHeader);

        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
        }

        String profileImageUrl = authService.updateProfileImage(email, profileImage);

        if (profileImageUrl!=null) {
        	Map<String, String> response = new HashMap<>();
            response.put("profileImageUrl", profileImageUrl);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("프로필 사진 변경 실패");
        }
    }

    //put
    @PutMapping("/nickname")
    public ResponseEntity<?> updateNickname(
        @RequestBody Map<String, String> request,
        @RequestHeader("Authorization") String authorizationHeader) {

        String email = authService.extractEmailFromToken(authorizationHeader);
        String newNickname = request.get("nickname");

        if (email == null || newNickname == null || newNickname.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Request");
        }

        boolean isSuccess = authService.updateNickname(email, newNickname);

        if (isSuccess) {
            return ResponseEntity.ok("닉네임이 변경되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("닉네임 변경 실패");
        }
    }
    @PutMapping("/password")
    public ResponseEntity<?> updatePassword(
        @RequestBody Map<String, String> request,
        @RequestHeader("Authorization") String authorizationHeader) {

        String email = authService.extractEmailFromToken(authorizationHeader);
        String currentPassword = request.get("currentPassword");
        String newPassword = request.get("newPassword");

        if (email == null || currentPassword == null || newPassword == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Request");
        }

        boolean isSuccess = authService.updatePassword(email, currentPassword, newPassword);

        if (isSuccess) {
            return ResponseEntity.ok("비밀번호가 변경되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호 변경 실패");
        }
    }

    //delete
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAccount(
        @RequestHeader("Authorization") String authorizationHeader) {

        String email = authService.extractEmailFromToken(authorizationHeader);

        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
        }

        boolean isSuccess = authService.deleteAccount(email);

        if (isSuccess) {
            return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원 탈퇴 실패");
        }
    }

}
