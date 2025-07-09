package org.example.bookmarket.common.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookmarket.user.entity.SocialType;
import org.example.bookmarket.user.entity.User;
import org.example.bookmarket.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttributeAdvice {

    private final UserRepository userRepository;

    @ModelAttribute
    public void addUserInfo(Model model, Authentication authentication) {
        model.addAttribute("userId", null);
        if (authentication == null || !authentication.isAuthenticated()) {
            return;
        }

        Object principal = authentication.getPrincipal();
        User user = null;
        if (principal instanceof UserDetails u) {
            user = (User) u;
            // fetch the latest user info to reflect profile updates immediately
            user = userRepository.findById(user.getId()).orElse(user);
        } else if (principal instanceof OAuth2User oauth2User) {
            Object idAttr = oauth2User.getAttribute("id");
            if (idAttr != null) {
                user = userRepository
                        .findBySocialTypeAndSocialId(SocialType.KAKAO, idAttr.toString())
                        .orElse(null);
            }
        }

        if (user != null) {
            model.addAttribute("userId", user.getId());
            model.addAttribute("nickname", user.getNickname());
            model.addAttribute("profileImageUrl", user.getProfileImageUrl());
        }
    }
}