package org.example.bookmarket.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter // user1Id를 컨트롤러에서 설정해야 하므로 @Setter 필요
@NoArgsConstructor // @RequestBody를 위해 기본 생성자 필요
public class ChatRequest {
    private Long user1Id;    // 구매자 ID (컨트롤러에서 @AuthenticationPrincipal로 설정)
    private Long user2Id;    // 판매자 ID (프론트에서 넘어옴)
    private Long usedBookId; // 중고책 ID (프론트에서 넘어옴)
}