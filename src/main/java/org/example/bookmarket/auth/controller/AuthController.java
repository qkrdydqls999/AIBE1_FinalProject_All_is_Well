package org.example.bookmarket.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
// [제거] 더 이상 사용하지 않는 LoginRequest, LoginResponse DTO를 import 목록에서 삭제합니다.
// import org.example.bookmarket.auth.dto.LoginRequest;
// import org.example.bookmarket.auth.dto.LoginResponse;
import org.example.bookmarket.auth.dto.SignUpRequest;
import org.example.bookmarket.auth.service.AuthService;
// [제거] ResponseEntity, @ResponseBody, @RequestBody, @PostMapping도 더 이상 필요 없습니다.
// import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // [제거] JWT 토큰을 반환하던 /api/login 엔드포인트를 완전히 삭제합니다.
    // 이 역할은 이제 SecurityConfig의 formLogin()이 자동으로 처리합니다.
    /*
    @PostMapping("/api/login")
    @ResponseBody
    public ResponseEntity<LoginResponse> apiLogin(@Valid @RequestBody LoginRequest request) {
        LoginResponse tokenInfo = authService.login(request);
        return ResponseEntity.ok(tokenInfo);
    }
    */

    // [페이지 반환] 로그인 페이지를 보여줍니다. (이 코드는 올바르며, 유지해야 합니다.)
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // "login.html" 템플릿을 렌더링합니다.
    }

    // [페이지 반환] 회원가입 페이지를 보여줍니다. (이 코드는 올바르며, 유지해야 합니다.)
    @GetMapping("/signup")
    public String signupPage() {
        return "signup"; // "signup.html" 템플릿을 렌더링합니다.
    }

    // [기능 처리] 회원가입을 처리합니다. (이 코드는 올바르며, 유지해야 합니다.)
    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute SignUpRequest request) {
        authService.signUp(request);
        // 회원가입 성공 후 로그인 페이지로 리다이렉트합니다.
        return "redirect:/auth/login";
    }
}