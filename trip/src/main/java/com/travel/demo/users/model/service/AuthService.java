package com.travel.demo.users.model.service;

import com.travel.demo.users.dto.UserLoginRequest;
import com.travel.demo.users.dto.UserSignUpRequest;

public  interface AuthService {
    String join(UserSignUpRequest joinInfo);
    String login(UserLoginRequest loginInfo);
}
