package org.example.bookmarket.wishlist.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookmarket.wishlist.dto.WishlistItem;
import org.example.bookmarket.wishlist.service.WishlistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @GetMapping
    public ResponseEntity<List<WishlistItem>> getWishlist(@RequestParam Long userId) {
        return ResponseEntity.ok(wishlistService.getItems(userId));
    }

    @PostMapping
    public ResponseEntity<Void> addItem(@RequestParam Long userId, @RequestParam Long usedBookId) {
        wishlistService.addItem(userId, usedBookId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{usedBookId}")
    public ResponseEntity<Void> removeItem(@RequestParam Long userId, @PathVariable Long usedBookId) {
        wishlistService.removeItem(userId, usedBookId);
        return ResponseEntity.noContent().build();
    }
}