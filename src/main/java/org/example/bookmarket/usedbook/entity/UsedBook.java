package org.example.bookmarket.usedbook.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.bookmarket.book.entity.Book;
import org.example.bookmarket.category.entity.Category;
import org.example.bookmarket.common.TimeEntity;
import org.example.bookmarket.user.entity.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "used_book")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 무분별한 생성을 막습니다.
public class UsedBook extends TimeEntity {

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
    private String detailedCondition;
    private Integer sellingPrice;
    private Integer aiSuggestedMinPrice;
    private Integer aiSuggestedMaxPrice;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "used_book_defects", joinColumns = @JoinColumn(name = "used_book_id"))
    @Column(name = "defect")
    private List<String> aiDetectedDefects;

    private String status;

    // NullPointerException 방지를 위해 초기화합니다.
    @OneToMany(mappedBy = "usedBook", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsedBookImage> images = new ArrayList<>();

    //  @Builder를 클래스가 아닌 생성자에 직접 적용하여 안정성을 높입니다.
    @Builder
    public UsedBook(User seller, Book book, Category category, String conditionGrade, String detailedCondition,
                    Integer sellingPrice, Integer aiSuggestedMinPrice, Integer aiSuggestedMaxPrice,
                    List<String> aiDetectedDefects, String status) {
        this.seller = seller;
        this.book = book;
        this.category = category;
        this.conditionGrade = conditionGrade;
        this.detailedCondition = detailedCondition;
        this.sellingPrice = sellingPrice;
        this.aiSuggestedMinPrice = aiSuggestedMinPrice;
        this.aiSuggestedMaxPrice = aiSuggestedMaxPrice;
        this.aiDetectedDefects = aiDetectedDefects;
        this.status = status;
    }

    public void setImages(List<UsedBookImage> images) {
        this.images.clear();
        if (images != null) {
            this.images.addAll(images);
            images.forEach(image -> image.setUsedBook(this)); // 양방향 연관관계 설정
        }
    }

    public void setSellingPrice(Integer sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public void setDetailedCondition(String detailedCondition) {
        this.detailedCondition = detailedCondition;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void markAsSold() {
        this.status = "판매 완료";
    }
}
