package com.travel.demo.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 사용자 정보를 반환하는 DTO.
 */
@Data
@Schema(title="User : 유저찾기 DTO")
public class FindUserResponse {

    @Schema(description = "사용자 이름", example = "John Doe")
    private String name;

    @Schema(description = "사용자 닉네임", example = "Traveler123")
    private String nickname;

    @Schema(description = "사용자 이메일", example = "user@example.com")
    private String email;
}
