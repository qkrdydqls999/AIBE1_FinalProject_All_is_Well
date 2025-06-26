package org.example.bookmarket.dm.repository;

import org.example.bookmarket.dm.entity.DmChannel;
import org.example.bookmarket.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DmChannelRepository extends JpaRepository<DmChannel, Long> {

    Optional<DmChannel> findByUser1AndUser2AndRelatedUsedBookId(User user1, User user2, Long usedBookId);

    List<DmChannel> findByUser1OrUser2(User user1, User user2);
}