package com.travel.demo.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


@Data
@NoArgsConstructor
@Schema(title="User : 회원가입 DTO")
public class UserSignUpRequest {
    @NotBlank(message = "아이디로 사용될 사용자 이메일을 입력해 주세요.")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    @Schema(description = "사용자 이메일", example = "usermail@gmail.com")
    private String email;   //이게 아이디

    @NotBlank
    @Schema(description = "비밀번호", example = "test123!")
//    @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
//            message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
    private String password;

    @NotBlank(message = "사용자 이름을 입력해주세요.")
    @Size(min = 3, max = 15, message = "사용자 이름은 15글자 이하로 입력해야 합니다.")
    @Schema(description = "사용자 이름", example = "홍길동")
    private String name;

    @NotBlank(message = "사용자 닉네임을 입력해주세요.")

    @Size(min = 3, max = 15, message = "사용자 닉네임은 15글자 이하로 입력해야 합니다.")
    @Schema(description = "사용자 닉네임", example = "경로당소드마스터")
    private String nickName;
    @Schema(description = "휴대폰 번호", example = "01012345678")
    private String phone;
    private boolean verified;
}
