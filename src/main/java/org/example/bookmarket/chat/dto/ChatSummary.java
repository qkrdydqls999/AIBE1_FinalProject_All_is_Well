
package org.example.bookmarket.chat.dto;

import java.time.LocalDateTime;

public record ChatSummary(
    Long channelId,
    String lastMessage,
    String partnerNickname,
    LocalDateTime updatedAt
) {}
