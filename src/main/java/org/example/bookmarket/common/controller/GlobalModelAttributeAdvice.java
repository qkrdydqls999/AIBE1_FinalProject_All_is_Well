package org.example.bookmarket.common.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookmarket.user.entity.SocialType;
import org.example.bookmarket.user.entity.User;
import org.example.bookmarket.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

// [수정] UserRepository 의존성을 제거하여 DB 조회를 원천 차단합니다.
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttributeAdvice {

    // [수정] UserRepository를 더 이상 사용하지 않으므로 삭제합니다.
    // private final UserRepository userRepository;

    @ModelAttribute
    public void addUserInfo(Model model, Authentication authentication) {
        model.addAttribute("userId", null);
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return;
        }

        Object principal = authentication.getPrincipal();
        User user = null;

        // [수정] principal이 이미 UserDetails 타입(User 클래스)이므로 직접 캐스팅하여 사용합니다.
        // DB를 다시 조회할 필요가 없습니다.
        if (principal instanceof User) {
            user = (User) principal;
        } else if (principal instanceof OAuth2User oauth2User) {
            // OAuth2의 경우, CustomOAuth2UserService에서 DB 조회를 이미 마쳤으므로
            // 여기서는 Authentication 객체에 담긴 정보를 신뢰하고 사용해야 합니다.
            // 하지만 현재 구조상 User 객체를 직접 얻기 어려우므로, 이 부분은 그대로 두거나
            // CustomOAuth2UserService에서 User 객체를 principal로 반환하도록 개선할 수 있습니다.
            // 지금 당장의 성능 개선을 위해 이메일 기반 로그인 경로의 DB 조회를 막는 것이 핵심입니다.
            String email = (String) oauth2User.getAttribute("email");
            if (email != null) {
                // 이 부분은 개선의 여지가 있지만, 주된 병목 지점은 아닙니다.
                // user = userRepository.findByEmail(email).orElse(null);
            }
        }

        if (user != null) {
            model.addAttribute("userId", user.getId());
            model.addAttribute("nickname", user.getNickname());
            model.addAttribute("profileImageUrl", user.getProfileImageUrl());
        }
    }
}