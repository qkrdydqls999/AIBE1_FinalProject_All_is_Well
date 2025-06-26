package org.example.bookmarket.chat.repository;

import org.example.bookmarket.chat.entity.ChatChannel;
import org.example.bookmarket.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatChannelRepository extends JpaRepository<ChatChannel, Long> {
    List<ChatChannel> findByUser1OrUser2(User user1, User user2);
}