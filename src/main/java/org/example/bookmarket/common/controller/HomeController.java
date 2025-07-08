package org.example.bookmarket.common.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookmarket.book.dto.BookResponse;
import org.example.bookmarket.usedbook.dto.UsedBookResponse;
import org.example.bookmarket.usedbook.service.UsedBookQueryService;
import org.example.bookmarket.user.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UsedBookQueryService usedBookQueryService;

    @GetMapping("/")
    public String root(Model model, Authentication authentication) {

        // 1. AI 맞춤 추천 도서 데이터
        List<BookResponse> recommendedBooks = List.of(
                new BookResponse(1L, "979-11-6224-484-9", "클린 코드", "로버트 C. 마틴", "인사이트", 2021, 32000, "설명...", "https://images.unsplash.com/photo-1544947950-fa07a98d237f?q=80&w=1887&auto=format&fit=crop"),
                new BookResponse(2L, "978-89-98139-76-6", "객체지향의 사실과 오해", "조영호", "위키북스", 2015, 20000, "설명...", "https://images.unsplash.com/photo-1532012197267-da84d127e765?q=80&w=1887&auto=format&fit=crop")
        );
        model.addAttribute("recommendedBooks", recommendedBooks);

        // 2. 방금 올라온 따끈한 책 데이터 - 최신순 4권
        List<UsedBookResponse> newlyAddedBooks = usedBookQueryService.getLatestUsedBooks(4);
        model.addAttribute("newlyAddedBooks", newlyAddedBooks);

        // 3. 지금 많이 찾는 책 (태그) 데이터
        List<String> popularTags = List.of("클린코드", "네트워크", "JPA", "운영체제", "HTTP");
        model.addAttribute("popularTags", popularTags);


        // 로그인 상태 확인 로직
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            String nickname = "게스트";
            String profileImageUrl = "https://images.unsplash.com/photo-1532074205216-d0e1f4b87368?q=80&w=1941&auto=format&fit=crop";

            if (principal instanceof UserDetails) {
                User user = (User) principal;
                nickname = user.getNickname();
                profileImageUrl = user.getProfileImageUrl();
            } else if (principal instanceof OAuth2User) {
                OAuth2User oauth2User = (OAuth2User) principal;
                Map<String, Object> attributes = oauth2User.getAttributes();
                Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
                Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
                if (profile != null) {
                    nickname = (String) profile.get("nickname");
                    profileImageUrl = (String) profile.get("profile_image_url");
                }
            }
            model.addAttribute("nickname", nickname);
            model.addAttribute("profileImageUrl", profileImageUrl);
        }

        return "home";
    }
}