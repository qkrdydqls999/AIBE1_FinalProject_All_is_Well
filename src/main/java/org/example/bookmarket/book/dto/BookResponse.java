package org.example.bookmarket.book.dto;

public record BookResponse(
    Long id,
    String isbn,
    String title,
    String author,
    String publisher,
    Integer publicationYear,
    Integer newPrice,
    String description,
    String coverImageUrl
) {}
