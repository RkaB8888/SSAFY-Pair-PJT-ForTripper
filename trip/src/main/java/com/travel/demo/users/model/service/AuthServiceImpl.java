package com.travel.demo.users.model.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.travel.demo.users.domain.UserDomain;
import com.travel.demo.users.dto.FindUserResponse;
import com.travel.demo.users.dto.UserLoginRequest;
import com.travel.demo.users.dto.UserSignUpRequest;
import com.travel.demo.users.entity.UserEntity;
import com.travel.demo.users.model.mapper.AuthMapper;
import com.travel.demo.util.JWTUtil;

import lombok.RequiredArgsConstructor;

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
    public Map<String, String> login(UserLoginRequest loginInfo) {
        String email = loginInfo.getEmail();
        String password = loginInfo.getPassword();

        System.out.println(email);

        UserEntity userEntity = authMapper.findByEmail(email);
        if(userEntity == null || !passwordEncoder.matches(password, userEntity.getPassword())) return null;

        UserDomain userDomain = new UserDomain();
        userDomain.setEmail(userEntity.getEmail());
        userDomain.setNickName(userEntity.getNickName());
        userDomain.setRole(userEntity.getRole());
        
     // Access Token 생성
        String accessToken = jwtUtil.generateToken(userDomain);

        // Refresh Token 생성
        String refreshToken = jwtUtil.generateRefreshToken(userDomain);

        // Access Token과 Refresh Token을 Map으로 반환
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access-token", accessToken);
        tokens.put("refresh-token", refreshToken);
        
        return tokens;
    }

	@Override
	public FindUserResponse findByEmail(String email) {
		UserEntity userEntity = authMapper.findByEmail(email);
		if(userEntity == null) return null;
		
		FindUserResponse findUserResponse = new FindUserResponse();
		findUserResponse.setEmail(userEntity.getEmail());
		findUserResponse.setName(userEntity.getName());
		findUserResponse.setNickname(userEntity.getNickName());
		return findUserResponse;
	}

	@Override
	public FindUserResponse findByNickName(String nickname) {
		UserEntity userEntity = authMapper.findByNickName(nickname);
		if(userEntity == null) return null;
		
		FindUserResponse findUserResponse = new FindUserResponse();
		findUserResponse.setEmail(userEntity.getEmail());
		findUserResponse.setName(userEntity.getName());
		findUserResponse.setNickname(userEntity.getNickName());
		return findUserResponse;
	}
}
