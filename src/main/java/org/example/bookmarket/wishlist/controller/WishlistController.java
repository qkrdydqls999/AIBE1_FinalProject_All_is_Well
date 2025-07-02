package org.example.bookmarket.wishlist.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookmarket.wishlist.dto.WishlistItem;
import org.example.bookmarket.wishlist.service.WishlistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping("/{usedBookId}")
    public ResponseEntity<Void> add(@PathVariable Long usedBookId, @RequestParam Long userId) {
        wishlistService.addToWishlist(userId, usedBookId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{usedBookId}")
    public ResponseEntity<Void> remove(@PathVariable Long usedBookId, @RequestParam Long userId) {
        wishlistService.removeFromWishlist(userId, usedBookId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<WishlistItem>> list(@RequestParam Long userId) {
        return ResponseEntity.ok(wishlistService.getWishlist(userId));
    }
}