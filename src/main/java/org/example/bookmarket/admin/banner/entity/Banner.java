package org.example.bookmarket.admin.banner.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.bookmarket.common.TimeEntity;

import org.example.bookmarket.admin.banner.entity.BannerStatus;

/**
 * Banner entity representing promotional banners.
 */

@Entity
@Table(name = "banner")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Banner extends TimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;
    private String link;
    private String title;
    private Integer sortOrder;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BannerStatus status = BannerStatus.ACTIVE;

    @Builder
    public Banner(String imageUrl, String link, String title, Integer sortOrder, BannerStatus status) {
            this.imageUrl = imageUrl;
            this.link = link;
            this.title = title;
            this.sortOrder = sortOrder;
            if (status != null) {
                this.status = status;
            }
        }

        public void changeStatus(BannerStatus status) {
            this.status = status;
        }
    }