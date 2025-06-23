package org.example.bookmarket.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

// 로그인 요청 시 사용할 DTO
public record LoginRequest(
        @NotBlank @Email
        @Schema(description = "사용자 이메일", example = "user@example.com")
        String email,

        @NotBlank
        @Schema(description = "비밀번호", example = "password123!")
        String password
) {}