package org.example.bookmarket.dm.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class DmChannelResponse {
    private Long channelId;
    private Long user1Id;
    private Long user2Id;
    private Long usedBookId;
    private LocalDateTime lastMessageAt;
    private LocalDateTime createdAt;
}