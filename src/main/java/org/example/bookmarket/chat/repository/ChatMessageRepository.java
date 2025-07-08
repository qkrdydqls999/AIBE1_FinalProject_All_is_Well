package org.example.bookmarket.chat.repository;

import org.example.bookmarket.chat.entity.ChatChannel;
import org.example.bookmarket.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List; // Import List
import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    /**
     * 특정 채팅 채널에서 가장 최근에 보내진 메시지 1개를 조회합니다.
     * ProfileServiceImpl에서 마지막 메시지를 표시하기 위해 사용됩니다.
     */
    Optional<ChatMessage> findFirstByChannelOrderBySentAtDesc(ChatChannel channel);

    /**
     * [FIX] 특정 채팅 채널의 모든 메시지를 보낸 시간(sentAt) 오름차순으로 조회합니다.
     * ChatService의 getMessages 메서드에서 채팅 내역을 불러오기 위해 사용됩니다.
     * @param channel 조회할 채팅 채널
     * @return ChatMessage 리스트
     */
    List<ChatMessage> findByChannelOrderBySentAtAsc(ChatChannel channel);
}