package org.example.bookmarket.dm.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.bookmarket.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "direct_message")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class DirectMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false)
    private DmChannel channel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String messageContent;

    private LocalDateTime sentAt;

    @Column(nullable = false)
    private Boolean isRead = false;

    @PrePersist
    public void prePersist() {
        this.sentAt = LocalDateTime.now();
    }

    public void markAsRead() {
        this.isRead = true;
    }
}