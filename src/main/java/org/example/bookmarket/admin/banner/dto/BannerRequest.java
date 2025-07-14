package org.example.bookmarket.admin.banner.dto;

import org.example.bookmarket.admin.banner.entity.BannerStatus;

/**
 * DTO used for banner form binding. Implemented as a record to allow
 * immutable values while still supporting Spring's data binding.
 */
public record BannerRequest(
        Long id,
        String imageUrl,
        String link,
        String title,
        Integer sortOrder,
        BannerStatus status
) {
    /**
     * Default constructor used by Spring when rendering an empty form.
     */
    public BannerRequest() {
        this(null, null, null, null, null, null);
    }
}