package org.example.bookmarket.common.controller;

import org.example.bookmarket.book.dto.BookResponse;
import lombok.RequiredArgsConstructor;
import org.example.bookmarket.user.entity.User;
import org.example.bookmarket.user.entity.SocialType;
import org.example.bookmarket.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UserRepository userRepository;

    @GetMapping("/")
    public String root(Model model, Authentication authentication) {

        // 1. AI 맞춤 추천 도서 데이터
        List<BookResponse> recommendedBooks = List.of(
                new BookResponse(1L, "979-11-6224-484-9", "클린 코드", "로버트 C. 마틴", "인사이트", 2021, 32000, "설명...", "https://images.unsplash.com/photo-1544947950-fa07a98d237f?q=80&w=1887&auto=format&fit=crop"),
                new BookResponse(2L, "978-89-98139-76-6", "객체지향의 사실과 오해", "조영호", "위키북스", 2015, 20000, "설명...", "https://images.unsplash.com/photo-1532012197267-da84d127e765?q=80&w=1887&auto=format&fit=crop")
        );
        model.addAttribute("recommendedBooks", recommendedBooks);

        // 2. 방금 올라온 따끈한 책 데이터
        List<BookResponse> newlyAddedBooks = List.of(
                new BookResponse(5L, "978-89-6848-198-7", "운영체제", "A. Silberschatz", "교보문고", 2018, 32000, "설명...", "https://images.unsplash.com/photo-1512820790803-83ca734da794?q=80&w=1798&auto=format&fit=crop"),
                new BookResponse(6L, "979-11-85890-57-0", "데이터베이스 시스템", "Korth", "한빛아카데미", 2021, 35000, "설명...", "https://images.unsplash.com/photo-1495446815901-a7297e633e8d?q=80&w=1770&auto=format&fit=crop")
        );
        model.addAttribute("newlyAddedBooks", newlyAddedBooks);

        // 3. 지금 많이 찾는 책 (태그) 데이터
        List<String> popularTags = List.of("클린코드", "네트워크", "JPA", "운영체제", "HTTP");
        model.addAttribute("popularTags", popularTags);


        // 로그인 상태 확인 로직
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User user) {
                model.addAttribute("nickname", user.getNickname());
                model.addAttribute("profileImageUrl", user.getProfileImageUrl());
            } else if (principal instanceof OAuth2User oauth2User) {
                Object idAttr = oauth2User.getAttribute("id");
                if (idAttr != null) {
                    userRepository.findBySocialTypeAndSocialId(SocialType.KAKAO, idAttr.toString())
                            .ifPresent(u -> {
                                model.addAttribute("nickname", u.getNickname());
                                model.addAttribute("profileImageUrl", u.getProfileImageUrl());
                            });
                }
            }
        }

        return "home";
    }
}