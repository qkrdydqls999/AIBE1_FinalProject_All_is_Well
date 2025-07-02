package org.example.bookmarket.profile.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.bookmarket.chat.dto.ChatSummary;
import org.example.bookmarket.profile.dto.ProfileResponse;
import org.example.bookmarket.trade.dto.PurchaseSummary;
import org.example.bookmarket.usedbook.dto.UsedBookSummary;
import org.example.bookmarket.wishlist.dto.WishlistItem;
import org.example.bookmarket.wishlist.service.WishlistService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfilePageController {

    private final WishlistService wishlistService;

    // 프로필 메인 페이지 (판매 목록이 기본으로 표시)
    @GetMapping("/main")
    public String profileMainPage(Model model, HttpServletRequest request) {
        addBaseProfileInfo(model); // 공통 프로필 정보 추가
        model.addAttribute("currentUri", request.getRequestURI());

        List<UsedBookSummary> salesList = List.of(
                new UsedBookSummary(101L, "클린 코드", 15000, "판매 완료", "Jane Smith", LocalDateTime.now().minusDays(1)),
                new UsedBookSummary(102L, "HTTP 완벽 가이드", 22000, "판매중", null, LocalDateTime.now().minusHours(5))
        );
        model.addAttribute("salesList", salesList);
        return "profile/main";
    }


    // 판매 목록 페이지
    @GetMapping("/sales")
    public String salesPage(Model model, HttpServletRequest request) {
        addBaseProfileInfo(model); // 공통 프로필 정보 추가
        model.addAttribute("currentUri", request.getRequestURI());

        List<UsedBookSummary> salesList = List.of(
                new UsedBookSummary(101L, "클린 코드", 15000, "판매 완료", "Jane Smith", LocalDateTime.now().minusDays(1)),
                new UsedBookSummary(102L, "HTTP 완벽 가이드", 22000, "판매중", null, LocalDateTime.now().minusHours(5))
        );
        model.addAttribute("salesList", salesList);
        return "profile/sales";
    }

    // 구매 목록 페이지
    @GetMapping("/purchases")
    public String purchasesPage(Model model, HttpServletRequest request) {
        addBaseProfileInfo(model);
        model.addAttribute("currentUri", request.getRequestURI());

        List<PurchaseSummary> purchasesList = List.of(
                // PurchaseSummary의 필드명에 맞게 데이터 전달
                new PurchaseSummary(201L, "객체지향의 사실과 오해", "SellerKim", 12000, "거래 완료", LocalDateTime.now().minusDays(3))
        );
        model.addAttribute("purchases", purchasesList);
        return "profile/purchases";
    }

    // DM 목록 페이지
    @GetMapping("/dm")
    public String dmPage(Model model, HttpServletRequest request) {
        addBaseProfileInfo(model);
        model.addAttribute("currentUri", request.getRequestURI());
        model.addAttribute("dmList", List.of());
        return "profile/dm";
    }

    // 위시리스트 페이지
    @GetMapping("/wishlist")
    public String wishlistPage(Model model, HttpServletRequest request) {
        addBaseProfileInfo(model);
        model.addAttribute("currentUri", request.getRequestURI());

        List<WishlistItem> wishlist = wishlistService.getItems();
        if (wishlist.isEmpty()) {
            wishlist = List.of(
                    new WishlistItem(301L, "이펙티브 자바", "https://images.unsplash.com/photo-1589829085413-56de8ae18c73?q=80&w=1887&auto=format&fit=crop", 25000, "판매자닉네임예시", LocalDateTime.now().minusDays(5))
            );
        }
        model.addAttribute("wishlist", wishlist);
        return "profile/wishlist";
    }

    // 모든 페이지에 공통으로 필요한 프로필 정보를 추가하는 헬퍼 메소드
    private void addBaseProfileInfo(Model model) {
        ProfileResponse profile = new ProfileResponse(
                "John Doe",
                "john.doe@example.com",
                "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?q=80&w=1780&auto=format&fit=crop", // profileImageUrl에 해당하는 값
                null
        );
        model.addAttribute("profile", profile);
    }
}