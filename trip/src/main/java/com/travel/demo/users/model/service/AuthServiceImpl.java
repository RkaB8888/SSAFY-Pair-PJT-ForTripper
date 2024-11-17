package com.travel.demo.users.model.service;

import com.travel.demo.users.domain.UserDomain;
import com.travel.demo.users.dto.UserLoginRequest;
import com.travel.demo.users.dto.UserSignUpRequest;
import com.travel.demo.users.entity.UserEntity;
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
    public String join(UserSignUpRequest joinInfo) {
        String encodedPassword = passwordEncoder.encode(joinInfo.getPassword());
        joinInfo.setPassword(encodedPassword);

        authMapper.join(joinInfo);
        UserEntity userEntity = authMapper.findByEmail(joinInfo.getEmail());
        UserDomain userDomain = new UserDomain();
        userDomain.setEmail(userEntity.getEmail());
        userDomain.setNickName(userEntity.getNickName());
        userDomain.setRole(userEntity.getRole());
        return jwtUtil.generateToken(userDomain);
    }

    @Override
    public String login(UserLoginRequest loginInfo) {
        String email = loginInfo.getEmail();
        String password = loginInfo.getPassword();

        System.out.println(email);

        UserEntity userEntity = authMapper.findByEmail(email);
        if(userEntity == null || !passwordEncoder.matches(password, userEntity.getPassword())) return null;

        UserDomain userDomain = new UserDomain();
        userDomain.setEmail(userEntity.getEmail());
        userDomain.setNickName(userEntity.getNickName());
        userDomain.setRole(userEntity.getRole());
        return jwtUtil.generateToken(userDomain);
    }
}
