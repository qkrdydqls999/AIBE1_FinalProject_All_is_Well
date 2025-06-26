package org.example.bookmarket.chat.repository;

import org.example.bookmarket.chat.entity.ChatChannel;
import org.example.bookmarket.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChannelOrderBySentAtAsc(ChatChannel channel);
}