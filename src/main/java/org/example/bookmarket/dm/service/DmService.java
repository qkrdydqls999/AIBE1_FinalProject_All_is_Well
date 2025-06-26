package org.example.bookmarket.dm.service;

import lombok.RequiredArgsConstructor;
import org.example.bookmarket.usedbook.entity.UsedBook;
import org.example.bookmarket.usedbook.repository.UsedBookRepository;
import org.example.bookmarket.dm.dto.*;
import org.example.bookmarket.dm.entity.*;
import org.example.bookmarket.dm.repository.*;
import org.example.bookmarket.user.entity.User;
import org.example.bookmarket.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DmService {

    private final DmChannelRepository dmChannelRepository;
    private final DirectMessageRepository directMessageRepository;
    private final UserRepository userRepository;
    private final UsedBookRepository usedBookRepository;

    public DmChannelResponse createChannel(DmChannelRequest request) {
        User user1 = userRepository.findById(request.getUser1Id())
                .orElseThrow(() -> new IllegalArgumentException("user1 없음"));
        User user2 = userRepository.findById(request.getUser2Id())
                .orElseThrow(() -> new IllegalArgumentException("user2 없음"));
        UsedBook usedBook = usedBookRepository.findById(request.getUsedBookId())
                .orElseThrow(() -> new IllegalArgumentException("usedBook 없음"));

        DmChannel channel = DmChannel.builder()
                .user1(user1)
                .user2(user2)
                .relatedUsedBook(usedBook)
                .build();

        DmChannel saved = dmChannelRepository.save(channel);

        return DmChannelResponse.builder()
                .channelId(saved.getId())
                .user1Id(user1.getId())
                .user2Id(user2.getId())
                .usedBookId(usedBook.getId())
                .createdAt(saved.getCreatedAt())
                .lastMessageAt(saved.getLastMessageAt())
                .build();
    }

    public List<DmChannelResponse> getChannelsForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        return dmChannelRepository.findByUser1OrUser2(user, user)
                .stream()
                .map(channel -> DmChannelResponse.builder()
                        .channelId(channel.getId())
                        .user1Id(channel.getUser1().getId())
                        .user2Id(channel.getUser2().getId())
                        .usedBookId(channel.getRelatedUsedBook().getId())
                        .createdAt(channel.getCreatedAt())
                        .lastMessageAt(channel.getLastMessageAt())
                        .build())
                .collect(Collectors.toList());
    }

    public List<DirectMessageResponse> getMessages(Long channelId) {
        DmChannel channel = dmChannelRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("채널 없음"));

        return directMessageRepository.findByChannelOrderBySentAtAsc(channel)
                .stream()
                .map(msg -> DirectMessageResponse.builder()
                        .messageId(msg.getId())
                        .senderId(msg.getSender().getId())
                        .content(msg.getMessageContent())
                        .isRead(msg.getIsRead())
                        .sentAt(msg.getSentAt())
                        .build())
                .collect(Collectors.toList());
    }
}