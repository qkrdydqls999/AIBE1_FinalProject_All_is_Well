package org.example.bookmarket.chat.repository;

import org.example.bookmarket.chat.entity.ChatChannel;
import org.example.bookmarket.usedbook.entity.UsedBook;
import org.example.bookmarket.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatChannelRepository extends JpaRepository<ChatChannel, Long> {

    /**
     * 특정 사용자가 참여하고 있는 모든 채팅 채널을 조회합니다.
     * ProfileServiceImpl과 ChatService에서 사용됩니다.
     * @param user1 사용자 1
     * @param user2 사용자 2 (보통 user1과 동일한 객체)
     * @return ChatChannel 리스트
     */
    List<ChatChannel> findByUser1OrUser2(User user1, User user2);

    /**
     * [해결] 두 명의 사용자와 특정 중고책에 연결된 채팅 채널이 있는지 조회합니다.
     * ChatService의 createChannel 메서드에서 중복 채널 생성을 방지하기 위해 사용됩니다.
     * @param user1 사용자 1
     * @param user2 사용자 2
     * @param relatedUsedBook 연관된 중고책
     * @return 존재할 경우 ChatChannel을 담은 Optional, 없으면 빈 Optional
     */
    Optional<ChatChannel> findByUser1AndUser2AndRelatedUsedBook(User user1, User user2, UsedBook relatedUsedBook);
}