package org.example.bookmarket.chat.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.bookmarket.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "DIRECT_MESSAGE")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "channel_id", nullable = false)
    private ChatChannel channel;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @Lob
    // ✅ [핵심 수정] columnDefinition = "TEXT" 속성을 추가합니다.
    // 이렇게 하면 JPA가 이 필드를 데이터베이스의 TEXT 타입과 올바르게 매핑합니다.
    @Column(name = "message_content", nullable = false, columnDefinition = "TEXT")
    private String messageContent;

    @Column(name = "is_read")
    private Boolean isRead;

    @Column(name = "sent_at", nullable = false)
    private LocalDateTime sentAt;

    @PrePersist
    protected void onSend() {
        this.sentAt = LocalDateTime.now();
        if (this.isRead == null) {
            this.isRead = false;
        }
    }
}