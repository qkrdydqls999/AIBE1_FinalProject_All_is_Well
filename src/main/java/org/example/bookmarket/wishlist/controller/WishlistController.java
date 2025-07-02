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
    public ResponseEntity<List<WishlistItem>> getWishlist() {
        return ResponseEntity.ok(wishlistService.getItems());
    }

    @PostMapping
    public ResponseEntity<Void> addItem(@RequestBody WishlistItem item) {
        wishlistService.addItem(item);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{usedBookId}")
    public ResponseEntity<Void> removeItem(@PathVariable Long usedBookId) {
        wishlistService.removeItem(usedBookId);
        return ResponseEntity.noContent().build();
    }
}