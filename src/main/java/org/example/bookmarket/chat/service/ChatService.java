package org.example.bookmarket.chat.service;

import lombok.RequiredArgsConstructor;
import org.example.bookmarket.chat.dto.*;
import org.example.bookmarket.chat.entity.ChatChannel;
import org.example.bookmarket.chat.entity.ChatMessage;
import org.example.bookmarket.chat.repository.ChatChannelRepository;
import org.example.bookmarket.chat.repository.ChatMessageRepository;
import org.example.bookmarket.common.handler.exception.CustomException;
import org.example.bookmarket.common.handler.exception.ErrorCode;
import org.example.bookmarket.usedbook.entity.UsedBook;
import org.example.bookmarket.usedbook.repository.UsedBookRepository;
import org.example.bookmarket.user.entity.User;
import org.example.bookmarket.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatChannelRepository chatChannelRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final UsedBookRepository usedBookRepository;

    /**
     * 채팅 채널을 생성하거나, 이미 존재하면 기존 채널을 반환합니다.
     * @param request 채팅 생성 요청 DTO
     * @return 생성되거나 조회된 채팅 채널 정보
     */
    @Transactional
    public ChatResponse createChannel(ChatRequest request) {
        User user1 = getUserById(request.getUser1Id());
        User user2 = getUserById(request.getUser2Id());
        UsedBook usedBook = usedBookRepository.findById(request.getUsedBookId())
                .orElseThrow(() -> new CustomException(ErrorCode.USED_BOOK_NOT_FOUND));

        // 중복 채널 생성을 방지하기 위해, 기존 채널이 있는지 먼저 확인하고 없으면 새로 생성합니다.
        ChatChannel channel = chatChannelRepository.findByUser1AndUser2AndRelatedUsedBook(user1, user2, usedBook)
                .orElseGet(() -> {
                    ChatChannel newChannel = ChatChannel.builder()
                            .user1(user1)
                            .user2(user2)
                            .relatedUsedBook(usedBook)
                            .build();
                    return chatChannelRepository.save(newChannel);
                });

        return toChatResponse(channel);
    }

    /**
     * 특정 사용자가 참여하고 있는 모든 채팅 채널 목록을 조회합니다.
     * @param userId 사용자 ID
     * @return 채팅 채널 목록
     */
    @Transactional(readOnly = true)
    public List<ChatResponse> getUserChannels(Long userId) {
        User user = getUserById(userId);

        return chatChannelRepository.findByUser1OrUser2(user, user)
                .stream()
                .map(this::toChatResponse)
                .collect(Collectors.toList());
    }

    /**
     * 특정 채팅 채널의 모든 메시지 내역을 조회합니다.
     * @param channelId 채널 ID
     * @return 메시지 목록
     */
    @Transactional(readOnly = true)
    public List<ChatMessageResponse> getMessages(Long channelId) {
        ChatChannel channel = chatChannelRepository.findById(channelId)
                // [수정] 새로 정의한, 명확한 에러 코드를 사용하여 코드를 간결하게 만듭니다.
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_CHANNEL_NOT_FOUND));

        return chatMessageRepository.findByChannelOrderBySentAtAsc(channel)
                .stream()
                .map(this::toChatMessageResponse)
                .collect(Collectors.toList());
    }

    // 중복 코드를 줄이고 가독성을 높이기 위한 private 헬퍼 메서드
    private User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    // 엔티티를 DTO로 변환하는 로직을 분리하여 관리합니다.
    private ChatResponse toChatResponse(ChatChannel channel) {
        return ChatResponse.builder()
                .channelId(channel.getId())
                .user1Id(channel.getUser1().getId())
                .user2Id(channel.getUser2().getId())
                .usedBookId(channel.getRelatedUsedBook().getId())
                .createdAt(channel.getCreatedAt())
                .lastMessageAt(channel.getLastMessageAt())
                .build();
    }

    private ChatMessageResponse toChatMessageResponse(ChatMessage msg) {
        return ChatMessageResponse.builder()
                .messageId(msg.getId())
                .senderId(msg.getSender().getId())
                .content(msg.getMessageContent())
                .isRead(msg.getIsRead())
                .sentAt(msg.getSentAt())
                .build();
    }
}