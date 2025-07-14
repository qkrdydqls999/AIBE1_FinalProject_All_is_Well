package org.example.bookmarket.chat.service;

import org.example.bookmarket.chat.dto.*;

import java.util.List;

/**
 * 채팅 관련 비즈니스 로직을 정의하는 서비스 인터페이스입니다.
 */
public interface ChatService {

    /**
     * 채팅 채널을 생성하거나, 이미 존재하면 기존 채널을 반환합니다.
     */
    ChatResponse createChannel(ChatRequest request);

    /**
     * 특정 사용자가 참여하고 있는 모든 채팅 채널 목록을 조회합니다.
     */
    List<ChatResponse> getUserChannels(Long userId);

    /**
     * 특정 채팅 채널의 모든 메시지 내역을 조회합니다.
     */
    List<ChatMessageResponse> getMessages(Long channelId);

    /**
     * WebSocket을 통해 수신된 메시지를 저장하고, 저장된 메시지 정보를 반환합니다.
     */
    ChatMessageResponse sendMessage(ChatMessageRequest request);

    /**
     * 메시지 삭제
     *
     * @param messageId     삭제할 메시지 ID
     * @param currentUserId 현재 로그인한 사용자 ID
     */
    void deleteMessage(Long messageId, Long currentUserId);

    /**
     * ✅ [추가] 채팅방 페이지에 필요한 정보를 조회하는 메서드 선언
     *
     * @param channelId     조회할 채널 ID
     * @param currentUserId 현재 로그인한 사용자 ID
     * @return 채팅방 정보(상대방 닉네임, 책 제목 등) DTO
     */
    ChatRoomInfo getChatRoomInfo(Long channelId, Long currentUserId);

    void completeTrade(Long channelId, Integer price, Long currentUserId);

    void cancelTrade(Long channelId, Long currentUserId);
}