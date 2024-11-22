package com.travel.demo.users.entity;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@Schema(title="User : Verify")
public class VerificationToken {
	private String token;
    private String userEmail; // 사용자의 이메일 참조
    private LocalDateTime expiryDate;
}
