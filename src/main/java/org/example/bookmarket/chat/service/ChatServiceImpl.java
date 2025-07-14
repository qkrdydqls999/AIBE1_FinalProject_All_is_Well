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
import org.example.bookmarket.trade.entity.Trade;
import org.example.bookmarket.trade.entity.TradeStatus;
import org.example.bookmarket.trade.service.TradeService;
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
    private final TradeService tradeService;

    @Override // ✅ @Override 어노테이션 추가
    @Transactional
    public ChatResponse createChannel(ChatRequest request) {
        User user1 = getUserById(request.getUser1Id());
        User user2 = getUserById(request.getUser2Id());
        UsedBook usedBook = usedBookRepository.findById(request.getUsedBookId())
                .orElseThrow(() -> new CustomException(ErrorCode.USED_BOOK_NOT_FOUND));

        ChatChannel channel = chatChannelRepository
                .findByUserParticipantsAndUsedBook(user1, user2, usedBook)
                .orElseGet(() -> {
                    ChatChannel newChannel = ChatChannel.builder()
                            .user1(user1)
                            .user2(user2)
                            .relatedUsedBook(usedBook)
                            .build();
                    return chatChannelRepository.save(newChannel);
                });
        try {
            tradeService.getTradeByChannel(channel.getId());
        } catch (CustomException e) {
            tradeService.createTrade(channel, usedBook, user2, user1);
        }
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

        Trade trade = tradeService.getTradeByChannel(channel.getId());
        if (trade.getStatus() != TradeStatus.REQUESTED) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }

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

    @Override
    @Transactional
    public void deleteMessage(Long messageId, Long currentUserId) {
        ChatMessage message = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_MESSAGE_NOT_FOUND));

        if (!message.getSender().getId().equals(currentUserId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_MESSAGE_DELETE);
        }

        chatMessageRepository.delete(message);
    }

    @Override // ✅ @Override 어노테이션 추가
    @Transactional(readOnly = true)
    public ChatRoomInfo getChatRoomInfo(Long channelId, Long currentUserId) {
        ChatChannel channel = chatChannelRepository.findById(channelId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_CHANNEL_NOT_FOUND));

        User user1 = channel.getUser1();
        User user2 = channel.getUser2();
        String partnerNickname;
        boolean isSeller;

        if (user1.getId().equals(currentUserId)) {
            partnerNickname = user2.getNickname();
            isSeller = false;
        } else if (user2.getId().equals(currentUserId)) {
            partnerNickname = user1.getNickname();
            isSeller = true;
        } else {
            throw new CustomException(ErrorCode.UNAUTHORIZED_CHAT_ACCESS);
        }

        String bookTitle = channel.getRelatedUsedBook().getBook().getTitle();
        Long bookId = channel.getRelatedUsedBook().getId();

        Trade trade = tradeService.getTradeByChannel(channelId);

        return ChatRoomInfo.builder()
                .partnerNickname(partnerNickname)
                .bookTitle(bookTitle)
                .bookId(bookId)
                .tradeStatus(trade.getStatus().name())
                .initialPrice(trade.getAgreedPrice())
                .build();
    }

    @Override
    @Transactional
    public void completeTrade(Long channelId, Integer price, Long currentUserId) {
        ChatChannel channel = chatChannelRepository.findById(channelId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_CHANNEL_NOT_FOUND));

        if (!channel.getUser2().getId().equals(currentUserId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_CHAT_ACCESS);
        }

        Trade trade = tradeService.getTradeByChannel(channelId);
        tradeService.completeTrade(trade.getId(), price);
    }

    @Override
    @Transactional
    public void cancelTrade(Long channelId, Long currentUserId) {
        ChatChannel channel = chatChannelRepository.findById(channelId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_CHANNEL_NOT_FOUND));

        if (!channel.getUser1().getId().equals(currentUserId) && !channel.getUser2().getId().equals(currentUserId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_CHAT_ACCESS);
        }

        Trade trade = tradeService.getTradeByChannel(channelId);
        tradeService.cancelTrade(trade.getId());
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