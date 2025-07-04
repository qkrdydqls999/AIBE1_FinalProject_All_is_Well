package org.example.bookmarket.wishlist.service;

import org.example.bookmarket.wishlist.dto.WishlistItem;

import java.util.List;

/**
 * Service interface for managing wishlist items.
 */
public interface WishlistService {
    /**
     * Retrieve all wishlist items of a user.
     */
    List<WishlistItem> getItems(Long userId);
    /**
     * Add a used book to the user's wishlist.
     */
    void addItem(Long userId, Long usedBookId);
    /**
     * Remove a used book from the user's wishlist.
     */
    void removeItem(Long userId, Long usedBookId);
}