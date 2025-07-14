package org.example.bookmarket.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.bookmarket.user.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminOnlyInterceptor implements HandlerInterceptor {

    public static final String ADMIN_EMAIL = "admin@gmail.com";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            Object principal = auth.getPrincipal();
            String email = null;
            if (principal instanceof UserDetails userDetails) {
                email = userDetails.getUsername();
            } else if (principal instanceof OAuth2User oAuth2User) {
                Object emailAttr = oAuth2User.getAttribute("email");
                if (emailAttr != null) {
                    email = emailAttr.toString();
                }
            } else if (principal instanceof User user) {
                email = user.getEmail();
            }
            if (ADMIN_EMAIL.equalsIgnoreCase(email)) {
                return true;
            }
        }
        response.sendRedirect("/");
        return false;
    }
}