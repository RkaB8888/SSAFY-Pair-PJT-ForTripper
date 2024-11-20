package com.travel.demo.users.model.service;

import java.util.Map;

import com.travel.demo.users.dto.FindUserResponse;
import com.travel.demo.users.dto.UserLoginRequest;
import com.travel.demo.users.dto.UserSignUpRequest;

public  interface AuthService {
    String join(UserSignUpRequest joinInfo);
    Map<String, String> login(UserLoginRequest loginInfo);
    FindUserResponse findByEmail(String email);
    FindUserResponse findByNickName(String nickname);
}
