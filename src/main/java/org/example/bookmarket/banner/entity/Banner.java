package org.example.bookmarket.banner.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.bookmarket.common.TimeEntity;

@Entity
@Table(name = "banner")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Banner extends TimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;
    private String link;
    private String title;
    private Integer sortOrder;

    @Builder
    public Banner(String imageUrl, String link, String title, Integer sortOrder) {
        this.imageUrl = imageUrl;
        this.link = link;
        this.title = title;
        this.sortOrder = sortOrder;
    }
}