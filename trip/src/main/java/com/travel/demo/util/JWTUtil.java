package com.travel.demo.util;

import java.util.Date;

import javax.crypto.SecretKey;

import com.travel.demo.users.domain.UserDomain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JWTUtil {

	@Value("${jwt.secret-key}")
	private String secretKeyPlain;

	private final long EXPIRATION_SECONDS = 12*60*60; // 토큰 유효기간 12시간
	private static final int REFRESH_EXPIRATION_SECONDS = 24*60*60*7; // 7일

	// application.properties에 등록된 변수
	public SecretKey getSecretKey() {
		return Keys.hmacShaKeyFor(secretKeyPlain.getBytes());
	}

	// Access Token 생성
	public String generateAccessToken(UserDomain user) {
		Date now = new Date();
		Date expiration = new Date(now.getTime() + EXPIRATION_SECONDS * 1000);

		return Jwts.builder().claim("id", user.getEmail()).claim("nickName", user.getNickName())
				.claim("role", user.getRole()).expiration(expiration) // 만료 시간
				.signWith(getSecretKey()).compact();
	}

	// Refresh Token 생성
	public String generateRefreshToken(UserDomain user) {
		Date now = new Date();
		Date expiration = new Date(now.getTime() + REFRESH_EXPIRATION_SECONDS * 1000); // 만료 시간 설정

		return Jwts.builder().claim("id", user.getEmail()).claim("nickName", user.getNickName())
				.claim("role", user.getRole()).expiration(expiration).signWith(getSecretKey()).compact();
	}

	// 토큰 유효성 검사
	public boolean isValid(String token) {
		try {
			Claims claims = Jwts.parser()
					.verifyWith(getSecretKey())
					.build()
					.parseSignedClaims(token)
					.getPayload();
			// 만료 시간 검사
			Date now = new Date();
			if (claims.getExpiration().before(now)) {
				return false;
			}

			// 추가적인 클레임 검사 (필요한 경우)
			// 예:
			// if (!claims.get("role", String.class).equals("ADMIN")) {
			// return false;
			// }

			return true;
		} catch (ExpiredJwtException ex) {
			log.warn("Token has expired: {}", ex.getMessage());
			return false;
		}
	}

	// 토큰으로 부터 ID 조회
	public String getIdFromToken(String token) {
		Claims claims = Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload();

		String id = (String) claims.get("id");
		log.debug("claim id:{}", id);
		return id;
	}
}
