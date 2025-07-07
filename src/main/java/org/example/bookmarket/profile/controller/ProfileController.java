package org.example.bookmarket.profile.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookmarket.chat.dto.ChatSummary;
import org.example.bookmarket.profile.dto.ProfileResponse;
import org.example.bookmarket.profile.dto.ProfileUpdateRequest;
import org.example.bookmarket.profile.service.ProfileService;
import org.example.bookmarket.trade.dto.PurchaseSummary;
import org.example.bookmarket.user.entity.User;
import org.example.bookmarket.usedbook.dto.UsedBookSummary;
import org.example.bookmarket.wishlist.dto.WishlistItem;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/profile") // [수정] /api 경로를 추가하여 페이지 URL과 완전히 분리합니다.
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    // [보안 강화] 모든 메소드에서 @RequestParam Long userId를 제거하고,
    // @AuthenticationPrincipal을 사용하여 안전하게 현재 로그인된 사용자 정보를 가져옵니다.

    @GetMapping("/me")
    public ResponseEntity<ProfileResponse> getMyProfile(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(profileService.getMyProfile(user.getId()));
    }

    @PatchMapping("/me")
    public ResponseEntity<Void> updateMyProfile(@AuthenticationPrincipal User user,
                                                @RequestBody ProfileUpdateRequest request) {
        profileService.updateMyProfile(user.getId(), request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/me/image")
    public ResponseEntity<String> uploadProfileImage(@AuthenticationPrincipal User user,
                                                     @RequestPart MultipartFile image) {
        return ResponseEntity.ok(profileService.uploadProfileImage(user.getId(), image));
    }

    @GetMapping("/me/dms")
    public ResponseEntity<List<ChatSummary>> getMyDmList(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(profileService.getMyDmList(user.getId()));
    }

    @GetMapping("/me/sell-books")
    public ResponseEntity<List<UsedBookSummary>> getMySellBooks(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(profileService.getMySellBooks(user.getId()));
    }

    @GetMapping("/me/purchases")
    public ResponseEntity<List<PurchaseSummary>> getMyPurchases(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(profileService.getMyPurchases(user.getId()));
    }

    @GetMapping("/me/wishlist")
    public ResponseEntity<List<WishlistItem>> getMyWishlist(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(profileService.getMyWishlist(user.getId()));
    }
}
