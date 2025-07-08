package org.example.bookmarket.wishlist.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookmarket.wishlist.dto.WishlistItem;
import org.example.bookmarket.wishlist.service.WishlistService;
import org.example.bookmarket.user.entity.SocialType;
import org.example.bookmarket.user.entity.User;
import org.example.bookmarket.user.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<WishlistItem>> getWishlist(Authentication authentication) {
        Long userId = resolveCurrentUser(authentication).getId();
        return ResponseEntity.ok(wishlistService.getItems(userId));
    }

    @PostMapping
    public ResponseEntity<Void> addItem(@RequestParam Long usedBookId, Authentication authentication) {
        Long userId = resolveCurrentUser(authentication).getId();
        wishlistService.addItem(userId, usedBookId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{usedBookId}")
    public ResponseEntity<Void> removeItem(@PathVariable Long usedBookId, Authentication authentication) {
        Long userId = resolveCurrentUser(authentication).getId();
        wishlistService.removeItem(userId, usedBookId);
        return ResponseEntity.noContent().build();
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