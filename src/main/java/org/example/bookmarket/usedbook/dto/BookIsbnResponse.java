package org.example.bookmarket.usedbook.dto;

import lombok.Builder;

// 외부 API 응답을 가공하여 프론트엔드로 전달할 DTO
@Builder
public record BookIsbnResponse(
        String title,
        String author,
        String publisher,
        String publicationYear, // 출판일을 문자열로 처리 (YYYYMMDD)
        Integer newPrice,       // 정가
        String imageUrl
) {}