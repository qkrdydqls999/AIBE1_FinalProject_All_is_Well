package org.example.bookmarket.dm.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class DirectMessageResponse {
    private Long messageId;
    private Long senderId;
    private String content;
    private Boolean isRead;
    private LocalDateTime sentAt;
}