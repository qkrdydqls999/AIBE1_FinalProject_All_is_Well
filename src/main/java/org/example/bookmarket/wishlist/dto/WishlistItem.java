package org.example.bookmarket.wishlist.dto;

public record WishlistItem(
    Long usedBookId,
    String title,
    String imageUrl,
    Integer price
) {}
