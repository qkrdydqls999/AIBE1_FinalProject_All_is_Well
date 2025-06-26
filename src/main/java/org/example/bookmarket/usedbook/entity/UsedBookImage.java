package org.example.bookmarket.usedbook.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "used_book_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsedBookImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "used_book_id", nullable = false)
    private UsedBook usedBook;

    @Column(nullable = false)
    private String imageUrl;

    private Integer orderIndex;
}
