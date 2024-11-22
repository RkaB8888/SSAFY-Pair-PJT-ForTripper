package com.travel.demo.users.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Schema(title="User : Domain")
public class UserDomain {
    @NotBlank(message = "아이디로 사용될 사용자 이메일을 입력해 주세요.")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    @Schema(description = "사용자 이메일(사용자가 실제 사용하는 ID)", example = "usermail@gmail.com")
    private String email;   //이게 아이디

    @NotBlank(message = "사용자 닉네임을 입력해주세요.")
    @Size(min = 2, max = 15, message = "사용자 닉네임은 15글자 이하로 입력해야 합니다.")
    @Schema(description = "사용자 닉네임", example = "경로당소드마스터")
    private String nickName;

    private String role;
}
