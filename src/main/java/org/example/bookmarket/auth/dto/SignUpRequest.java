package org.example.bookmarket.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// 회원가입 요청 시 클라이언트가 보낼 데이터를 담는 클래스
public record SignUpRequest(
        @NotBlank(message = "이메일은 필수 입력값입니다.")
        @Email(message = "유효한 이메일 형식이 아닙니다.")
        @Schema(description = "사용자 이메일 (로그인 ID)", example = "user@example.com")
        String email,

        @NotBlank(message = "비밀번호는 필수 입력값입니다.")
        @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
        @Schema(description = "비밀번호 (8자 이상)", example = "password123!")
        String password,

        @NotBlank(message = "닉네임은 필수 입력값입니다.")
        @Size(min = 2, max = 15, message = "닉네임은 2자 이상 15자 이하로 입력해주세요.")
        @Schema(description = "사용자 닉네임", example = "북마켓유저")
        String nickname
) {}