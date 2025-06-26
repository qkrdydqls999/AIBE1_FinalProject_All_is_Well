package org.example.bookmarket.chat.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatResponse {
    private Long channelId;
    private Long user1Id;
    private Long user2Id;
    private Long usedBookId;
    private LocalDateTime lastMessageAt;
    private LocalDateTime createdAt;
}