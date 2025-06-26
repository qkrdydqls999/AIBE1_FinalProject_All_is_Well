package org.example.bookmarket.wishlist.dto;

import java.time.LocalDateTime;

public record WishlistItem(
        Long usedBookId,
        String title,
        String imageUrl,
        Integer price,
        String sellerNickname, 
        LocalDateTime createdAt
) {}

