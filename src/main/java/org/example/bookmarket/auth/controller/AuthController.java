package org.example.bookmarket.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookmarket.auth.dto.SignUpRequest;
import org.example.bookmarket.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    // 로그인 페이지를 보여주는 메소드
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // templates/login.html
    }

    // 회원가입 페이지를 보여주는 메소드
    @GetMapping("/signup")
    public String signupPage(Model model) {
        // Thymeleaf 폼에서 사용할 수 있도록 빈 DTO 객체를 모델에 추가
        model.addAttribute("signUpRequest", new SignUpRequest(null, null, null));
        return "signup"; // templates/signup.html
    }

    // 회원가입 요청을 처리하는 메소드
    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute("signUpRequest") SignUpRequest request, BindingResult bindingResult, Model model) {
        // 1. 유효성 검사(Validation)에 실패하면 에러와 함께 가입 페이지로 다시 이동
        if (bindingResult.hasErrors()) {
            return "signup";
        }

        try {
            // 2. 서비스 로직 호출
            userService.signUp(request);
        } catch (IllegalArgumentException e) {
            // 3. 서비스에서 중복 이메일 등 예외가 발생하면 에러 메시지를 모델에 담아 가입 페이지로 이동
            model.addAttribute("errorMessage", e.getMessage());
            return "signup";
        }

        // 4. 회원가입 성공 시 로그인 페이지로 이동
        return "redirect:/auth/login";
    }
}