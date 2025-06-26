package org.example.bookmarket.dm.repository;

import org.example.bookmarket.dm.entity.DirectMessage;
import org.example.bookmarket.dm.entity.DmChannel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DirectMessageRepository extends JpaRepository<DirectMessage, Long> {

    List<DirectMessage> findByChannelOrderBySentAtAsc(DmChannel channel);
}