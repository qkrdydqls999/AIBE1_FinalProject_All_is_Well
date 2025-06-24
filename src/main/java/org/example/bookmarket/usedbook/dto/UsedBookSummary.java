
package org.example.bookmarket.usedbook.dto;

import java.time.LocalDateTime;

public record UsedBookSummary(
    Long usedBookId,
    String title,
    Integer price,
    String status,
    String buyerNickname,
    LocalDateTime updatedAt
) {}
