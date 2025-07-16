package org.example.bookmarket.chat.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.bookmarket.user.entity.User;
import org.example.bookmarket.usedbook.entity.UsedBook;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
// ✅ [수정] 테이블 이름을 DB 스키마에 맞게 "DM_CHANNEL"로 변경합니다.
@Table(name = "dm_channel")
public class ChatChannel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user1_id", nullable = false)
    private User user1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user2_id", nullable = false)
    private User user2;

    @ManyToOne(fetch = FetchType.LAZY)
    // ✅ [수정] 컬럼 이름을 DB 스키마에 맞게 "related_used_book_id"로 변경합니다.
    @JoinColumn(name = "related_used_book_id")
    private UsedBook relatedUsedBook;

    @Column(name = "last_message_at") // ✅ 스키마에 맞게 컬럼명 지정
    private LocalDateTime lastMessageAt;

    @Column(name = "created_at", nullable = false) // ✅ 스키마에 맞게 컬럼명 지정
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.lastMessageAt = now;
    }

    public void updateLastMessageAt(LocalDateTime now) {
        this.lastMessageAt = now;
    }
}