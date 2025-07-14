package org.example.bookmarket.chat.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookmarket.chat.dto.ChatMessageResponse;
import org.example.bookmarket.chat.dto.ChatRequest;
import org.example.bookmarket.chat.dto.ChatResponse;
import org.example.bookmarket.chat.dto.ChatRoomInfo;
import org.example.bookmarket.chat.service.ChatService;
import org.example.bookmarket.common.handler.exception.CustomException;
import org.example.bookmarket.common.handler.exception.ErrorCode;
import org.example.bookmarket.user.entity.SocialType;
import org.example.bookmarket.user.entity.User;
import org.example.bookmarket.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final UserRepository userRepository;

    /**
     * 채널 생성 또는 조회 (book-detail.html에서 호출)
     */
    @PostMapping("/channel")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ChatResponse createOrGetChannel(
            @RequestBody ChatRequest request,
            Authentication authentication
    ) {
        User currentUser = resolveCurrentUser(authentication);
        if (currentUser == null) {
            throw new CustomException(ErrorCode.LOGIN_REQUIRED);
        }
        request.setUser1Id(currentUser.getId());

        return chatService.createChannel(request);
    }

    /**
     * WebSocket 채팅방 뷰(chatroom.html)를 반환합니다.
     */
    @GetMapping("/room/{channelId}")
    public String getChatRoomPage(@PathVariable Long channelId,
                                  Model model,
                                  Authentication authentication) {

        User currentUser = resolveCurrentUser(authentication);

        // [수정] currentUser가 null일 경우를 반드시 확인합니다.
        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        ChatRoomInfo chatRoomInfo = chatService.getChatRoomInfo(channelId, currentUser.getId());

        model.addAttribute("channelId", channelId);
        model.addAttribute("userId", currentUser.getId());
        model.addAttribute("partnerNickname", chatRoomInfo.getPartnerNickname());
        model.addAttribute("bookTitle", chatRoomInfo.getBookTitle());
        model.addAttribute("bookUrl", "/used-books/" + chatRoomInfo.getBookId());

        return "chatroom";
    }

    /**
     * 사용자의 모든 채팅 채널 목록을 조회합니다.
     */
    @GetMapping("/channels/{userId}")
    @ResponseBody
    public List<ChatResponse> getUserChannels(@PathVariable Long userId) {
        return chatService.getUserChannels(userId);
    }

    /**
     * 특정 채널의 모든 메시지 내역을 조회합니다.
     */
    @GetMapping("/channel/{channelId}/messages")
    @ResponseBody
    public List<ChatMessageResponse> getMessages(@PathVariable Long channelId) {
        return chatService.getMessages(channelId);
    }

    /**
     * 일반 로그인과 소셜 로그인을 모두 처리하여 현재 로그인된 User 엔티티를 반환하는 헬퍼 메서드
     */
    private User resolveCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            // 로그인하지 않은 경우 null 반환
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof User) {
            return (User) principal;
        } else if (principal instanceof OAuth2User) {
            OAuth2User oauth2User = (OAuth2User) principal;
            String socialId = oauth2User.getAttribute("id").toString();
            return userRepository.findBySocialTypeAndSocialId(SocialType.KAKAO, socialId)
                    .orElse(null); // 사용자를 찾지 못한 경우 null 반환
        }

        return null;
    }
}