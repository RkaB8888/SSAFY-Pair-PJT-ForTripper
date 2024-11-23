package com.travel.demo.users.model.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.travel.demo.users.domain.UserDomain;
import com.travel.demo.users.dto.UserLoginRequest;
import com.travel.demo.users.dto.UserSignUpRequest;
import com.travel.demo.users.entity.PasswordResetToken;
import com.travel.demo.users.entity.UserEntity;
import com.travel.demo.users.entity.VerificationToken;
import com.travel.demo.users.model.mapper.AuthMapper;
import com.travel.demo.users.model.mapper.PasswordResetTokenMapper;
import com.travel.demo.users.model.mapper.VerificationTokenMapper;
import com.travel.demo.util.JWTUtil;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final AuthMapper authMapper;
	private final EmailService emailService;
	private final VerificationTokenMapper verificationTokenMapper;
	private final JWTUtil jwtUtil;
	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	private final PasswordResetTokenMapper passwordResetTokenMapper; // 비밀번호 재설정 토큰 매퍼 추가

	@Value("${app.frontend-url}")
	private String frontendUrl;

	@Override
	public String join(UserSignUpRequest joinInfo) {
		String encodedPassword = passwordEncoder.encode(joinInfo.getPassword());
		joinInfo.setPassword(encodedPassword);

		// 사용자를 인증되지 않은 상태로 설정
		joinInfo.setVerified(false);

		// 이메일 인증 토큰 생성
		String token = UUID.randomUUID().toString();

		VerificationToken verificationToken = new VerificationToken();
		verificationToken.setToken(token);
		verificationToken.setUserEmail(joinInfo.getEmail());
		verificationToken.setExpiryDate(LocalDateTime.now().plusHours(1));

		// 토큰 생성 후
		System.out.println("생성된 토큰: " + verificationToken.getToken());
		System.out.println("토큰 만료 시간: " + verificationToken.getExpiryDate());

		// 인증 이메일 전송
		String verificationUrl = "http://localhost:9000/trip/users/verify?token=" + token;

		String subject = "이메일 인증";
		String body = "<p>회원가입을 해주셔서 감사합니다. 아래 링크를 클릭하여 이메일을 인증해주세요:</p>" + "<a href=\"" + verificationUrl
				+ "\">이메일 인증하기</a>";

		// 이메일 발송
		try {
			emailService.sendVerificationEmail(joinInfo.getEmail(), subject, body);
		} catch (MessagingException e) {
			e.printStackTrace();
			System.err.println("이메일 전송 실패: " + e.getMessage());
			return null; // 이메일 전송 실패 시 메서드 종료
		}
		// DB에 사용자 정보 저장
		authMapper.join(joinInfo);

		// 사용자 정보를 가져와 토큰 발급
		UserEntity userEntity = authMapper.findByEmail(joinInfo.getEmail());
		UserDomain userDomain = new UserDomain();
		userDomain.setEmail(userEntity.getEmail());
		userDomain.setNickName(userEntity.getNickName());
		userDomain.setRole(userEntity.getRole());

		// 이메일 발송 성공 시 토큰 저장
		verificationTokenMapper.saveToken(verificationToken);

		// JWT 액세스 토큰 생성 및 반환
		return jwtUtil.generateAccessToken(userDomain);
	}

	@Override
	public Map<String, String> login(UserLoginRequest loginInfo) {
		String email = loginInfo.getEmail();
		String password = loginInfo.getPassword();

		System.out.println(email);

		UserEntity userEntity = authMapper.findByEmail(email);
		if (userEntity == null || !passwordEncoder.matches(password, userEntity.getPassword()))
			return null;

		if (userEntity.getIsVerified() == 0) {
			// 사용자가 이메일을 인증하지 않음
			System.out.println("이메일 인증을 안함");
			return null; // 또는 예외를 던져 메시지를 전달
		}

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
		if (userEntity == null)
			return null;

		UserDomain userDomain = new UserDomain();
		userDomain.setEmail(userEntity.getEmail());
		userDomain.setName(userEntity.getName());
		userDomain.setNickName(userEntity.getNickName());
		userDomain.setRole(userEntity.getRole());
		System.out.println("AuthService의 findByEmail 리턴 : " + userDomain);
		return userDomain;
	}

	@Override
	public UserDomain findByNickName(String nickname) {
		UserEntity userEntity = authMapper.findByNickName(nickname);
		if (userEntity == null)
			return null;

		UserDomain userDomain = new UserDomain();
		userDomain.setEmail(userEntity.getEmail());
		userDomain.setName(userEntity.getName());
		userDomain.setNickName(userEntity.getNickName());
		userDomain.setRole(userEntity.getRole());
		System.out.println("AuthService의 findByEmail 리턴 : " + userDomain);
		return userDomain;
	}

	@Override
	public boolean isValid(String token) {
		return jwtUtil.isValid(token);
	}

	@Override
	public String generateNewAccessToken(String refreshToken) {
		// 리프레시 토큰에서 아이디 추출
		String email = jwtUtil.getIdFromToken(refreshToken);
		// 이메일로 유저 도메인 획득
		UserDomain userDomain = findByEmail(email);
		// 유저 정보를 바탕으로 새로운 토큰 생성 및 반환
		String accessToken = jwtUtil.generateAccessToken(userDomain);
		System.out.println("새로 생성된 accessToken : " + accessToken);
		return accessToken;
	}

	@Override
	public boolean verifyEmail(String token) {
		VerificationToken verificationToken = verificationTokenMapper.findByToken(token);

		if (verificationToken == null || verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
			return false; // 유효하지 않거나 만료된 토큰
		}

		// 사용자를 찾아서 인증 상태로 변경
		UserEntity userEntity = authMapper.findByEmail(verificationToken.getUserEmail());
		if (userEntity == null) {
			return false;
		}

		userEntity.setIsVerified(1);
		authMapper.updateUserVerificationStatus(userEntity);

		// 인증 후 토큰 삭제
		verificationTokenMapper.deleteToken(token);

		return true;
	}

	@Override
	public boolean createPasswordResetToken(String email) {
		UserEntity userEntity = authMapper.findByEmail(email);
		if (userEntity == null) {
			return false; // 사용자 없음
		}

		// 비밀번호 재설정 토큰 생성
		String token = UUID.randomUUID().toString();

		PasswordResetToken resetToken = new PasswordResetToken();
		resetToken.setToken(token);
		resetToken.setUserEmail(email);
		resetToken.setExpiryDate(LocalDateTime.now().plusHours(1)); // 토큰 만료 시간 설정

		passwordResetTokenMapper.saveToken(resetToken);

		// 이메일 전송
		String resetUrl = frontendUrl + "/auth/reset-password?token=" + token;
		String subject = "비밀번호 재설정 요청";
		String body = "<p>비밀번호 재설정을 요청하셨습니다. 아래 링크를 클릭하여 새 비밀번호를 설정해주세요:</p>" + "<a href=\"" + resetUrl
				+ "\">비밀번호 재설정하기</a>";

		try {
			emailService.sendVerificationEmail(email, subject, body);
		} catch (MessagingException e) {
			e.printStackTrace();
			return false; // 이메일 전송 실패 처리
		}

		return true;
	}

	@Override
	public boolean resetPassword(String token, String newPassword) {
		PasswordResetToken resetToken = passwordResetTokenMapper.findByToken(token);
		if (resetToken == null || resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
			return false; // 유효하지 않거나 만료된 토큰
		}

		// 비밀번호 업데이트
		UserEntity userEntity = authMapper.findByEmail(resetToken.getUserEmail());
		if (userEntity == null) {
			return false;
		}

		String encodedPassword = passwordEncoder.encode(newPassword);
		userEntity.setPassword(encodedPassword);
		authMapper.updatePassword(userEntity);

		// 토큰 삭제
		passwordResetTokenMapper.deleteToken(token);

		return true;
	}
}
