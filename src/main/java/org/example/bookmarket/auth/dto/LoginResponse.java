package org.example.bookmarket.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

// 로그인 성공 시 클라이언트에게 돌려줄 JWT 토큰을 담는 클래스
public record LoginResponse(
        @Schema(description = "발급된 Access Token", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIiwiaWF0IjoxNzE4Nzg...")
        String accessToken
) {}