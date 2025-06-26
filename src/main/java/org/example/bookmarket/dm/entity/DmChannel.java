package org.example.bookmarket.dm.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.bookmarket.user.entity.User;
import org.example.bookmarket.usedbook.entity.UsedBook;

import java.time.LocalDateTime;

@Entity
@Table(name = "dm_channel")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class DmChannel {

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
    @JoinColumn(name = "related_used_book_id")
    private UsedBook relatedUsedBook;

    private LocalDateTime lastMessageAt;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public void updateLastMessageAt(LocalDateTime time) {
        this.lastMessageAt = time;
    }
}