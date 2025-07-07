package org.example.bookmarket.wishlist.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.bookmarket.common.TimeEntity;
import org.example.bookmarket.usedbook.entity.UsedBook;
import org.example.bookmarket.user.entity.User;

@Table(name = "wishlist", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "used_book_id"})
})
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Wishlist extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "used_book_id", nullable = false)
    private UsedBook usedBook;

    @Builder
    public Wishlist(User user, UsedBook usedBook) {
        this.user = user;
        this.usedBook = usedBook;
    }
}