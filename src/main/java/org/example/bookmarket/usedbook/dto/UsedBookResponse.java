package org.example.bookmarket.usedbook.dto;

public record UsedBookResponse(
        Long id,
        Long bookId,
        String isbn,
        String title,
        String author,
        String publisher,
        Integer publicationYear,
        String conditionGrade,
        String detailedCondition,
        Integer sellingPrice,
        String status,
        Long categoryId,
        Long sellerId,
        String sellerNickname,
        String sellerProfileImageUrl,
        String coverImageUrl,
        String summary,
        String personaReview
) {}