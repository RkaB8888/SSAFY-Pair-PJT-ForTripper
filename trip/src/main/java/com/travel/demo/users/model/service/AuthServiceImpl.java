package com.travel.demo.users.model.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.travel.demo.users.domain.UserDomain;
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
        return jwtUtil.generateAccessToken(userDomain);
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
        String accessToken = jwtUtil.generateAccessToken(userDomain);

        // Refresh Token 생성
        String refreshToken = jwtUtil.generateRefreshToken(userDomain);

        // Access Token과 Refresh Token을 Map으로 반환
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access-token", accessToken);
        tokens.put("refresh-token", refreshToken);
        
        return tokens;
    }

	@Override
	public UserDomain findByEmail(String email) {
		UserEntity userEntity = authMapper.findByEmail(email);
		if(userEntity == null) return null;
		
		UserDomain userDomain = new UserDomain();
		userDomain.setEmail(userEntity.getEmail());
		userDomain.setName(userEntity.getName());
		userDomain.setNickName(userEntity.getNickName());
		userDomain.setRole(userEntity.getRole());
		System.out.println("AuthService의 findByEmail 리턴 : "+userDomain);
		return userDomain;
	}

	@Override
	public UserDomain findByNickName(String nickname) {
		UserEntity userEntity = authMapper.findByNickName(nickname);
		if(userEntity == null) return null;
		
		UserDomain userDomain = new UserDomain();
		userDomain.setEmail(userEntity.getEmail());
		userDomain.setName(userEntity.getName());
		userDomain.setNickName(userEntity.getNickName());
		return userDomain;
	}

	@Override
	public boolean isValid(String token) {
		return jwtUtil.isValid(token);
	}

	@Override
	public String generateNewAccessToken(String refreshToken) {
		//리프레시 토큰에서 아이디 추출
		String email = jwtUtil.getIdFromToken(refreshToken);
		//이메일로 유저 도메인 획득
		UserDomain userDomain = findByEmail(email);
		//유저 정보를 바탕으로 새로운 토큰 생성 및 반환
		String accessToken = jwtUtil.generateAccessToken(userDomain);
		System.out.println("새로 생성된 accessToken : "+accessToken);
		return accessToken;
	}
}
