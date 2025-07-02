package org.example.bookmarket.wishlist.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.bookmarket.common.TimeEntity;
import org.example.bookmarket.usedbook.entity.UsedBook;
import org.example.bookmarket.user.entity.User;

@Entity
@Table(name = "wishlist")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}