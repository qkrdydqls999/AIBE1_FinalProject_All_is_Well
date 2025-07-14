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

@Service // ✅ @Service 어노테이션 추가
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService { // ✅ ChatService 인터페이스 구현

    private final ChatChannelRepository chatChannelRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final UsedBookRepository usedBookRepository;

    @Override // ✅ @Override 어노테이션 추가
    @Transactional
    public ChatResponse createChannel(ChatRequest request) {
        User user1 = getUserById(request.getUser1Id());
        User user2 = getUserById(request.getUser2Id());
        UsedBook usedBook = usedBookRepository.findById(request.getUsedBookId())
                .orElseThrow(() -> new CustomException(ErrorCode.USED_BOOK_NOT_FOUND));

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

    @Override // ✅ @Override 어노테이션 추가
    @Transactional(readOnly = true)
    public List<ChatResponse> getUserChannels(Long userId) {
        User user = getUserById(userId);

        return chatChannelRepository.findByUser1OrUser2(user, user)
                .stream()
                .map(this::toChatResponse)
                .collect(Collectors.toList());
    }

    @Override // ✅ @Override 어노테이션 추가
    @Transactional(readOnly = true)
    public List<ChatMessageResponse> getMessages(Long channelId) {
        ChatChannel channel = chatChannelRepository.findById(channelId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_CHANNEL_NOT_FOUND));

        return chatMessageRepository.findByChannelOrderBySentAtAsc(channel)
                .stream()
                .map(this::toChatMessageResponse)
                .collect(Collectors.toList());
    }

    @Override // ✅ @Override 어노테이션 추가
    @Transactional
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

        channel.updateLastMessageAt(message.getSentAt());
        chatChannelRepository.save(channel);

        return toChatMessageResponse(message);
    }

    @Override // ✅ @Override 어노테이션 추가
    @Transactional(readOnly = true)
    public ChatRoomInfo getChatRoomInfo(Long channelId, Long currentUserId) {
        ChatChannel channel = chatChannelRepository.findById(channelId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_CHANNEL_NOT_FOUND));

        User user1 = channel.getUser1();
        User user2 = channel.getUser2();
        String partnerNickname;

        if (user1.getId().equals(currentUserId)) {
            partnerNickname = user2.getNickname();
        } else if (user2.getId().equals(currentUserId)) {
            partnerNickname = user1.getNickname();
        } else {
            throw new CustomException(ErrorCode.UNAUTHORIZED_CHAT_ACCESS);
        }

        String bookTitle = channel.getRelatedUsedBook().getBook().getTitle();
        Long bookId = channel.getRelatedUsedBook().getId();

        return ChatRoomInfo.builder()
                .partnerNickname(partnerNickname)
                .bookTitle(bookTitle)
                .bookId(bookId)
                .build();
    }

    // --- Private Helper Methods ---

    private User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

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