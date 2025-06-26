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
    private String messageContent;

    private Boolean isRead;

    private LocalDateTime sentAt;

    @PrePersist
    protected void onSend() {
        this.sentAt = LocalDateTime.now();
        if (this.isRead == null) {
            this.isRead = false;
        }
    }
}