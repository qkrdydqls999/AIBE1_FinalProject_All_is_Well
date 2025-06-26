package org.example.bookmarket.usedbook.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record UsedBookPostRequest(
    String isbn,
    String title,
    String author,
    String publisher,
    Integer publicationYear,
    String conditionGrade,
    boolean hasWriting,
    boolean hasStains,
    boolean hasTears,
    boolean hasWaterDamage,
    boolean likeNew,
    String detailedCondition,
    Integer sellingPrice,
    Long categoryId,
    List<MultipartFile> images
) {}
