package com.travel.demo.users.model.service;

import com.travel.demo.users.dto.User;
import com.travel.demo.users.model.mapper.AuthMapper;
import com.travel.demo.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final AuthMapper authMapper;
    private final JWTUtil jwtUtil;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public String join(User joinInfo) {
        String encodedPassword = passwordEncoder.encode(joinInfo.getPassword());
        joinInfo.setPassword(encodedPassword);

        authMapper.join(joinInfo);
        return jwtUtil.generateToken(joinInfo);
    }

    @Override
    public String login(User loginInfo) {
        String id = loginInfo.getEmail();
        String password = loginInfo.getPassword();

        User user = authMapper.findByEmail(id);
        if(user == null || !passwordEncoder.matches(password, user.getPassword())) return null;

        return jwtUtil.generateToken(user);
    }
}
