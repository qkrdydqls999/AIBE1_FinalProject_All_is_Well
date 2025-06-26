package org.example.bookmarket.chat.service;

import lombok.RequiredArgsConstructor;
import org.example.bookmarket.chat.dto.*;
import org.example.bookmarket.chat.entity.ChatChannel;
import org.example.bookmarket.chat.entity.ChatMessage;
import org.example.bookmarket.chat.repository.ChatChannelRepository;
import org.example.bookmarket.chat.repository.ChatMessageRepository;
import org.example.bookmarket.usedbook.entity.UsedBook;
import org.example.bookmarket.usedbook.repository.UsedBookRepository;
import org.example.bookmarket.user.entity.User;
import org.example.bookmarket.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatChannelRepository chatChannelRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final UsedBookRepository usedBookRepository;

    public ChatResponse createChannel(ChatRequest request) {
        User user1 = getUserById(request.getUser1Id());
        User user2 = getUserById(request.getUser2Id());
        UsedBook usedBook = usedBookRepository.findById(request.getUsedBookId())
                .orElseThrow(() -> new IllegalArgumentException("중고 도서 없음"));

        ChatChannel channel = ChatChannel.builder()
                .user1(user1)
                .user2(user2)
                .relatedUsedBook(usedBook)
                .build();

        return toChatResponse(chatChannelRepository.save(channel));
    }

    public List<ChatResponse> getUserChannels(Long userId) {
        User user = getUserById(userId);

        return chatChannelRepository.findByUser1OrUser2(user, user)
                .stream()
                .map(this::toChatResponse)
                .collect(Collectors.toList());
    }

    public List<ChatMessageResponse> getMessages(Long channelId) {
        ChatChannel channel = chatChannelRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("채널 없음"));

        return chatMessageRepository.findByChannelOrderBySentAtAsc(channel)
                .stream()
                .map(this::toChatMessageResponse)
                .collect(Collectors.toList());
    }

    // 🔧 리팩토링된 공통 메서드
    private User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));
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