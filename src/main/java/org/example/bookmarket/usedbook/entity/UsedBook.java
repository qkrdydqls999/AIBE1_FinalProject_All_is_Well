package org.example.bookmarket.usedbook.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.bookmarket.book.entity.Book;
import org.example.bookmarket.category.entity.Category;
import org.example.bookmarket.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "used_books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsedBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private User seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    private String conditionGrade;
    private boolean hasWriting;
    private boolean hasStains;
    private boolean hasTears;
    private boolean hasWaterDamage;
    private boolean likeNew;
    private String detailedCondition;

    private Integer sellingPrice;
    private Integer aiSuggestedMinPrice;
    private Integer aiSuggestedMaxPrice;

    private String status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "usedBook", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsedBookImage> images;
}
