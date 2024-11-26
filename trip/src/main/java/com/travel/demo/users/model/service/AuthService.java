package com.travel.demo.users.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.travel.demo.users.domain.UserDomain;
import com.travel.demo.users.dto.UserLoginRequest;
import com.travel.demo.users.dto.UserSignUpRequest;

public  interface AuthService {
    String join(UserSignUpRequest joinInfo);
    Map<String, String> login(UserLoginRequest loginInfo);
    UserDomain findByEmail(String email);
    UserDomain findByNickName(String nickname);
    boolean isValid(String token);
    String generateNewAccessToken(String refreshToken);
    boolean verifyEmail(String token);
    boolean createPasswordResetToken(String email);
    boolean resetPassword(String token, String newPassword);
	String getToken(String authorizationHeader);
	String extractEmailFromToken(String authorizationHeader);
	String updateProfileImage(String email, MultipartFile profileImage);
	boolean updateNickname(String email, String newNickname);
	boolean updatePassword(String email, String currentPassword, String newPassword);
	boolean deleteAccount(String email);
	List<UserDomain> searchUsersByNickname(String nickname);

}
