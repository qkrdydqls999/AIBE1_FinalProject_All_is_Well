package org.example.bookmarket.wishlist.service;

import org.example.bookmarket.wishlist.dto.WishlistItem;

import java.util.List;

public interface WishlistService {
    void addToWishlist(Long userId, Long usedBookId);
    void removeFromWishlist(Long userId, Long usedBookId);
    List<WishlistItem> getWishlist(Long userId);
}