
package org.example.bookmarket.profile.controller;

import org.example.bookmarket.profile.dto.ProfileResponse;
import org.example.bookmarket.profile.dto.ProfileUpdateRequest;
import org.example.bookmarket.profile.service.ProfileService;
import org.example.bookmarket.chat.dto.ChatSummary;
import org.example.bookmarket.usedbook.dto.UsedBookSummary;
import org.example.bookmarket.trade.dto.PurchaseSummary;
import org.example.bookmarket.wishlist.dto.WishlistItem;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

//    private final ProfileService profileService;

//    @GetMapping("/me")
//    public ResponseEntity<ProfileResponse> getMyProfile(@RequestParam Long userId) {
//        return ResponseEntity.ok(profileService.getMyProfile(userId));
//    }
//
//    @PatchMapping("/me")
//    public ResponseEntity<Void> updateMyProfile(@RequestParam Long userId,
//                                                @RequestBody ProfileUpdateRequest request) {
//        profileService.updateMyProfile(userId, request);
//        return ResponseEntity.noContent().build();
//    }

//    @GetMapping("/me/dms")
//    public ResponseEntity<List<ChatSummary>> getMyDmList(@RequestParam Long userId) {
//        return ResponseEntity.ok(profileService.getMyDmList(userId));
//    }
//
//    @GetMapping("/me/sell-books")
//    public ResponseEntity<List<UsedBookSummary>> getMySellBooks(@RequestParam Long userId) {
//        return ResponseEntity.ok(profileService.getMySellBooks(userId));
//    }
//
//    @GetMapping("/me/purchases")
//    public ResponseEntity<List<PurchaseSummary>> getMyPurchases(@RequestParam Long userId) {
//        return ResponseEntity.ok(profileService.getMyPurchases(userId));
//    }
//
//    @GetMapping("/me/wishlist")
//    public ResponseEntity<List<WishlistItem>> getMyWishlist(@RequestParam Long userId) {
//        return ResponseEntity.ok(profileService.getMyWishlist(userId));
//    }
}
