package com.travel.demo.users.model.service;

import java.util.Map;

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
}
