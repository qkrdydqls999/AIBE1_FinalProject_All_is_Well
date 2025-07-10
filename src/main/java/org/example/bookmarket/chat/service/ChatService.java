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
import org.example.bookmarket.chat.dto.ChatMessageRequest;
import org.example.bookmarket.chat.dto.ChatMessageResponse;
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
     * 이 메서드는 구매자(user1Id), 판매자(user2Id), 책(usedBookId) 기준으로 채널을 찾거나 생성합니다.
     * @param request 채팅 생성 요청 DTO (user1Id는 컨트롤러에서 설정됨)
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

    public ChatMessageResponse sendMessage(ChatMessageRequest request) {
        ChatChannel channel = chatChannelRepository.findById(request.getChannelId())
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_CHANNEL_NOT_FOUND));
        User sender = getUserById(request.getSenderId());

        ChatMessage message = ChatMessage.builder()
                .channel(channel)
                .sender(sender)
                .messageContent(request.getContent())
                .isRead(false)
                .build();

        chatMessageRepository.save(message);

        // 채널에 최신 메시지 시간 갱신
        channel.updateLastMessageAt(message.getSentAt());
        chatChannelRepository.save(channel);

        return toChatMessageResponse(message);
    }

    /**
     * ✅ [새로 추가] 채팅방 페이지에 필요한 정보를 조회합니다.
     * @param channelId 조회할 채널 ID
     * @param currentUserId 현재 로그인한 사용자 ID
     * @return 채팅방 정보 DTO
     */
    @Transactional(readOnly = true)
    public ChatRoomInfo getChatRoomInfo(Long channelId, Long currentUserId) {
        ChatChannel channel = chatChannelRepository.findById(channelId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_CHANNEL_NOT_FOUND));

        // 상대방 닉네임 찾기: 채널의 user1, user2 중 현재 로그인한 user가 아닌 다른 user를 찾습니다.
        User user1 = channel.getUser1();
        User user2 = channel.getUser2();
        String partnerNickname;

        if (user1.getId().equals(currentUserId)) {
            partnerNickname = user2.getNickname(); // 현재 사용자가 user1이면 상대방은 user2
        } else if (user2.getId().equals(currentUserId)) {
            partnerNickname = user1.getNickname(); // 현재 사용자가 user2이면 상대방은 user1
        } else {
            // 현재 로그인한 사용자가 이 채널의 참여자가 아닌 경우 (보안상 중요한 에러 처리)
            throw new CustomException(ErrorCode.UNAUTHORIZED_CHAT_ACCESS); // 예시 에러 코드
        }

        // 책 제목 가져오기
        String bookTitle = channel.getRelatedUsedBook().getBook().getTitle();
        Long bookId = channel.getRelatedUsedBook().getId(); // UsedBook의 ID는 그대로 사용

        return ChatRoomInfo.builder()
                .partnerNickname(partnerNickname)
                .bookTitle(bookTitle)
                .bookId(bookId)
                .build();
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