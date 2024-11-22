package com.travel.demo.users.entity;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(title="User : Reset")
public class PasswordResetToken {
    private String token;
    private String userEmail;
    private LocalDateTime expiryDate;

}
