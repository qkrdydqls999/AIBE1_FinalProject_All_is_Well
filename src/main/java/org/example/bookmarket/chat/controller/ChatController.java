package org.example.bookmarket.chat.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookmarket.chat.dto.ChatMessageResponse;
import org.example.bookmarket.chat.dto.ChatRequest;
import org.example.bookmarket.chat.dto.ChatResponse;
import org.example.bookmarket.chat.dto.ChatRoomInfo;
import org.example.bookmarket.chat.service.ChatService;
import org.example.bookmarket.common.handler.exception.CustomException;
import org.example.bookmarket.common.handler.exception.ErrorCode;
import org.example.bookmarket.security.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    // 📌 채널 생성 또는 조회 (book-detail.html에서 호출)
    @PostMapping("/channel")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody // JSON 응답 명시
    public ChatResponse createOrGetChannel(
            @RequestBody ChatRequest request, // usedBookId, user2Id(판매자ID)가 포함됨
            @AuthenticationPrincipal CustomUserDetails userDetails // 현재 로그인한 사용자 (구매자)
    ) {
        // 로그인되지 않은 사용자 처리
        if (userDetails == null || userDetails.getUser() == null) {
            throw new CustomException(ErrorCode.CHAT_CHANNEL_NOT_FOUND); // 적절한 예외 처리
        }
        // ChatRequest에 user1Id (구매자 ID)를 설정합니다.
        // 프론트에서 넘어온 user1Id는 무시하고, 로그인된 사용자 ID를 사용합니다.
        request.setUser1Id(userDetails.getUser().getId()); // 로그인된 구매자 ID 설정

        return chatService.createChannel(request); // createChannel 메서드가 이미 채널 생성/조회 역할 수행
    }

    // 📌 유저의 채널 리스트 조회
    @GetMapping("/channels/{userId}")
    @ResponseBody
    public List<ChatResponse> getUserChannels(@PathVariable Long userId) {
        return chatService.getUserChannels(userId);
    }

    // 📌 특정 채널의 메시지 리스트 조회
    @GetMapping("/channel/{channelId}/messages")
    @ResponseBody
    public List<ChatMessageResponse> getMessages(@PathVariable Long channelId) {
        return chatService.getMessages(channelId);
    }

    // ✅ chatroom.html 반환 (WebSocket 채팅방 뷰)
    // URL: /api/chat/room/{channelId}
    @GetMapping("/room/{channelId}")
    public String getChatRoomPage(@PathVariable Long channelId,
                                  Model model,
                                  @AuthenticationPrincipal CustomUserDetails userDetails) {
        // 로그인 상태 확인
        if (userDetails == null || userDetails.getUser() == null) {
            return "redirect:/login"; // 로그인되어 있지 않다면 로그인 페이지로 리다이렉트
        }

        // 📌 채널 정보 조회 (상대방 닉네임, 책 제목 등)
        ChatRoomInfo chatRoomInfo = chatService.getChatRoomInfo(channelId, userDetails.getUser().getId());

        model.addAttribute("channelId", channelId);
        model.addAttribute("userId", userDetails.getUser().getId()); // 현재 로그인한 사용자 ID (채팅 메시지 구분용)
        model.addAttribute("partnerNickname", chatRoomInfo.getPartnerNickname());
        model.addAttribute("bookTitle", chatRoomInfo.getBookTitle());
        model.addAttribute("bookUrl", "/books/" + chatRoomInfo.getBookId()); // 책 상세 페이지로 가는 링크

        return "chat/chatroom"; // 👉 templates/chat/chatroom.html 로 연결
    }
}