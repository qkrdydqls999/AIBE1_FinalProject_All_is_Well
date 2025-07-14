
package org.example.bookmarket.chat.dto;

import java.time.LocalDateTime;

public record ChatSummary(
    Long channelId,
    String partnerNickname,
    String bookTitle,
    String lastMessage,
    LocalDateTime updatedAt
) {}
