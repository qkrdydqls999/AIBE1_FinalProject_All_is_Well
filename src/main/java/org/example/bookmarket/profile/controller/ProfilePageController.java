package org.example.bookmarket.profile.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookmarket.profile.service.ProfileService;
import org.example.bookmarket.user.entity.User;
import org.example.bookmarket.user.entity.SocialType;
import org.example.bookmarket.user.repository.UserRepository;
import org.example.bookmarket.category.service.CategoryService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 프로필 관련 HTML 페이지를 렌더링하는 전용 컨트롤러입니다.
 * 모든 메소드는 실제 서비스(ProfileService)와 연동하여 동작합니다.
 */
@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfilePageController {

    private final ProfileService profileService;
    private final UserRepository userRepository;
    private final CategoryService categoryService;

    /**
     * 내 프로필 메인 페이지를 보여줍니다. (판매 목록이 기본)
     * @AuthenticationPrincipal: URL에 userId를 노출하는 대신, 스프링 시큐리티가 현재 로그인된 사용자 정보를 안전하게 주입해줍니다.
     */
    @GetMapping("/me")
    public String getMyProfilePage(Model model, Authentication authentication) {
        User user = resolveCurrentUser(authentication);
        if (user == null) {
            return "redirect:/auth/login";
        }

        // ProfileService를 통해 실제 DB 데이터를 조회합니다.
        model.addAttribute("profile", profileService.getMyProfile(user.getId()));
        model.addAttribute("salesList", profileService.getMySellBooks(user.getId()));
        model.addAttribute("activeTab", "sell-books"); // View에서 활성화된 탭을 표시하기 위한 값

        return "profile/main"; // templates/profile/main.html
    }

    /**
     * 프로필 수정 폼을 보여줍니다.
     */
    @GetMapping("/me/edit")
    public String getEditProfilePage(Model model, Authentication authentication) {
        User user = resolveCurrentUser(authentication);
        if (user == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("profile", profileService.getMyProfile(user.getId()));
        model.addAttribute("categories", categoryService.getAllCategories());
        return "profile/edit";
    }

    /**
     * 내 판매 목록 페이지를 보여줍니다.
     */
    @GetMapping("/me/sell-books") // [수정] API 경로와 일관성을 위해 'sales'를 'sell-books'로 변경
    public String getMySalesPage(Model model, Authentication authentication) {
        User user = resolveCurrentUser(authentication);
        if (user == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("profile", profileService.getMyProfile(user.getId()));
        model.addAttribute("salesList", profileService.getMySellBooks(user.getId()));
        model.addAttribute("activeTab", "sell-books");

        return "profile/sales"; // templates/profile/sales.html
    }

    /**
     * 내 구매 목록 페이지를 보여줍니다.
     */
    @GetMapping("/me/purchases")
    public String getMyPurchasesPage(Model model, Authentication authentication) {
        User user = resolveCurrentUser(authentication);
        if (user == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("profile", profileService.getMyProfile(user.getId()));
        model.addAttribute("purchases", profileService.getMyPurchases(user.getId()));
        model.addAttribute("activeTab", "purchases");

        return "profile/purchases"; // templates/profile/purchases.html
    }

    /**
     * 내 채팅(DM) 목록 페이지를 보여줍니다.
     */
    @GetMapping("/me/dms")
    public String getMyDmsPage(Model model, Authentication authentication) {
        User user = resolveCurrentUser(authentication);
        if (user == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("profile", profileService.getMyProfile(user.getId()));
        model.addAttribute("dmList", profileService.getMyDmList(user.getId()));
        model.addAttribute("activeTab", "dms");

        return "profile/dm"; // templates/profile/dm.html
    }

    /**
     * 내 위시리스트 페이지를 보여줍니다.
     */
    @GetMapping("/me/wishlist")
    public String getMyWishlistPage(Model model, Authentication authentication) {
        User user = resolveCurrentUser(authentication);
        if (user == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("profile", profileService.getMyProfile(user.getId()));
        model.addAttribute("wishlist", profileService.getMyWishlist(user.getId()));
        model.addAttribute("activeTab", "wishlist");

        return "profile/wishlist"; // templates/profile/wishlist.html
    }

    private User resolveCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof User user) {
            return user;
        } else if (principal instanceof OAuth2User oauth2User) {
            String socialId = oauth2User.getAttribute("id").toString();
            return userRepository.findBySocialTypeAndSocialId(SocialType.KAKAO, socialId).orElse(null);
        }
        return null;
    }
}