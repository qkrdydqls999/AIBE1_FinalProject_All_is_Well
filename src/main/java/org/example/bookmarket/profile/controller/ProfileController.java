package org.example.bookmarket.profile.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookmarket.chat.dto.ChatSummary;
import org.example.bookmarket.profile.dto.ProfileResponse;
import org.example.bookmarket.profile.dto.ProfileUpdateRequest;
import org.example.bookmarket.profile.service.ProfileService;
import org.example.bookmarket.trade.dto.PurchaseSummary;
import org.example.bookmarket.user.entity.SocialType;
import org.example.bookmarket.user.entity.User;
import org.example.bookmarket.usedbook.dto.UsedBookSummary;
import org.example.bookmarket.user.repository.UserRepository;
import org.example.bookmarket.wishlist.dto.WishlistItem;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import java.util.List;

@RestController
@RequestMapping("/api/profile") // [수정] /api 경로를 추가하여 페이지 URL과 완전히 분리합니다.
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;
    private final UserRepository userRepository;

    // 모든 메소드에서 @RequestParam Long userId를 제거하고,
    // @AuthenticationPrincipal을 사용하여 안전하게 현재 로그인된 사용자 정보를 가져옵니다.

    @GetMapping("/me")
    public ResponseEntity<ProfileResponse> getMyProfile(Authentication authentication) {
        User user = resolveCurrentUser(authentication);
        return ResponseEntity.ok(profileService.getMyProfile(user.getId()));
    }

    @PatchMapping("/me")
    public ResponseEntity<Void> updateMyProfile(Authentication authentication,
                                                @RequestBody ProfileUpdateRequest request) {
        User user = resolveCurrentUser(authentication);
        profileService.updateMyProfile(user.getId(), request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadProfileImage(Authentication authentication,
                                                     @RequestPart("image") MultipartFile image){
        User user = resolveCurrentUser(authentication);
        return ResponseEntity.ok(profileService.uploadProfileImage(user.getId(), image));
    }

    @GetMapping("/me/dms")
    public ResponseEntity<List<ChatSummary>> getMyDmList(Authentication authentication) {
        User user = resolveCurrentUser(authentication);
        return ResponseEntity.ok(profileService.getMyDmList(user.getId()));
    }

    @GetMapping("/me/sell-books")
    public ResponseEntity<List<UsedBookSummary>> getMySellBooks(Authentication authentication) {
        User user = resolveCurrentUser(authentication);
        return ResponseEntity.ok(profileService.getMySellBooks(user.getId()));
    }

    @GetMapping("/me/purchases")
    public ResponseEntity<List<PurchaseSummary>> getMyPurchases(Authentication authentication) {
        User user = resolveCurrentUser(authentication);
        return ResponseEntity.ok(profileService.getMyPurchases(user.getId()));
    }

    @GetMapping("/me/wishlist")
    public ResponseEntity<List<WishlistItem>> getMyWishlist(Authentication authentication) {
        User user = resolveCurrentUser(authentication);
        return ResponseEntity.ok(profileService.getMyWishlist(user.getId()));
    }

    private User resolveCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new IllegalStateException("Login required");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof User user) {
            return user;
        } else if (principal instanceof OAuth2User oauth2User) {
            String socialId = oauth2User.getAttribute("id").toString();
            return userRepository.findBySocialTypeAndSocialId(SocialType.KAKAO, socialId)
                    .orElseThrow(() -> new IllegalStateException("User not found"));
        }
        throw new IllegalStateException("Authentication failed");
    }
}
