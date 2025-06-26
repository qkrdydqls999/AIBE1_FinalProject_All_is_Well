package org.example.bookmarket.chat.dto;

import lombok.Getter;

@Getter
public class ChatRequest {
    private Long user1Id;
    private Long user2Id;
    private Long usedBookId;
}