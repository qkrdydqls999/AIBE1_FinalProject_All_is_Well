package org.example.bookmarket.chat.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatMessageResponse {
    private Long messageId;
    private Long senderId;
    private String content;
    private Boolean isRead;
    private LocalDateTime sentAt;
}