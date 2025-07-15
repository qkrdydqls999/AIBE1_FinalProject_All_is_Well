package org.example.bookmarket.chat.dto; // chat 패키지 내에 생성

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomInfo {
    private String partnerNickname;
    private String bookTitle;
    private Long bookId; // "책 보러가기" 링크를 위해 추가
    private boolean seller;
    private String tradeStatus;
    private Integer initialPrice;
}