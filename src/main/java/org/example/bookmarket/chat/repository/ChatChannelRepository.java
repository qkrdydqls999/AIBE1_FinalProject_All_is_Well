package org.example.bookmarket.chat.repository;

import org.example.bookmarket.chat.entity.ChatChannel;
import org.example.bookmarket.usedbook.entity.UsedBook;
import org.example.bookmarket.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatChannelRepository extends JpaRepository<ChatChannel, Long> {

    /**
     * ✅ [새로 추가] 사용자와 중고책 기준으로 채팅 채널을 조회 (user1, user2 순서 무관)
     * ChatService의 createChannel 메서드에서 중복 채널 생성을 방지하기 위해 사용됩니다.
     * @param userA 사용자 A
     * @param userB 사용자 B
     * @param usedBook 연관된 중고책
     * @return 존재할 경우 ChatChannel을 담은 Optional, 없으면 빈 Optional
     */
    @Query("SELECT c FROM ChatChannel c WHERE " +
            "(c.user1 = :userA AND c.user2 = :userB AND c.relatedUsedBook = :usedBook) OR " +
            "(c.user1 = :userB AND c.user2 = :userA AND c.relatedUsedBook = :usedBook)")
    Optional<ChatChannel> findByUserParticipantsAndUsedBook(
            @Param("userA") User userA,
            @Param("userB") User userB,
            @Param("usedBook") UsedBook usedBook);

    // 기존 메서드들은 그대로 유지합니다.
    List<ChatChannel> findByUser1OrUser2(User user1, User user2);
    Optional<ChatChannel> findByUser1AndUser2AndRelatedUsedBook(User user1, User user2, UsedBook relatedUsedBook);
}