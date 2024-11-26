package com.travel.demo.friends.controller;


import com.travel.demo.friends.model.service.FriendService;
import com.travel.demo.users.domain.UserDomain;
import com.travel.demo.users.model.service.AuthService;

import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FreindController {
	private final AuthService authService;
	private final FriendService friendService;
   
	@GetMapping("/")
	public ResponseEntity<?> getFriendList(
	    @RequestHeader("Authorization") String authorizationHeader) {
	    String userEmail = authService.extractEmailFromToken(authorizationHeader);
	    System.out.println("친구 목록 요청 받음");
	    if (userEmail == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
	    }
	    List<UserDomain> friends = friendService.getFriends(userEmail);
	    return ResponseEntity.ok(friends);
	}
	
	@GetMapping("/status/{nickname}")
	public ResponseEntity<?> checkFriendStatus(
	    @PathVariable("nickname") String nickname,
	    @RequestHeader("Authorization") String authorizationHeader) {
		System.out.println("친구인지 확인");
	    String userEmail = authService.extractEmailFromToken(authorizationHeader);
	    if (userEmail == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
	    }
	    boolean isFriend = friendService.isFriend(userEmail, nickname);
	    return ResponseEntity.ok(Map.of("isFriend", isFriend));
	}

	
    @PostMapping("/{nickname}")
    public ResponseEntity<?> addFriend(
        @PathVariable("nickname") String nickname,
        @RequestHeader("Authorization") String authorizationHeader) {
        String fromUserEmail = authService.extractEmailFromToken(authorizationHeader);
        if (fromUserEmail == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
        }
        boolean success = friendService.addFriend(fromUserEmail, nickname);
        if (success) {
            return ResponseEntity.ok("친구 요청을 보냈습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("친구 요청 실패");
        }
    }

    @DeleteMapping("/{nickname}")
    public ResponseEntity<?> removeFriend(
        @PathVariable("nickname") String nickname,
        @RequestHeader("Authorization") String authorizationHeader) {
        String fromUserEmail = authService.extractEmailFromToken(authorizationHeader);
        if (fromUserEmail == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
        }
        boolean success = friendService.removeFriend(fromUserEmail, nickname);
        if (success) {
            return ResponseEntity.ok("친구를 해제하였습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("친구 해제 실패");
        }
    }

}
