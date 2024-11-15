package com.travel.demo.users.model.service;

import com.travel.demo.users.dto.User;

public  interface AuthService {
    String join(User joinInfo);
    String login(User loginInfo);
}
