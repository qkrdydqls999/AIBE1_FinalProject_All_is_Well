package org.example.bookmarket.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookmarket.auth.JwtTokenProvider;
import org.example.bookmarket.auth.dto.LoginRequest;
import org.example.bookmarket.auth.dto.LoginResponse;
import org.example.bookmarket.auth.dto.SignUpRequest;
import org.example.bookmarket.auth.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

// @RestController -> @Controller로 변경
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // JWT 로그인을 위한 API는 그대로 유지하되, 혼동을 피하기 위해 경로를 변경하거나
    // 웹 로그인만 사용할 경우 주석 처리/삭제할 수 있습니다.
    // 여기서는 @ResponseBody를 추가하여 이 메소드만 JSON을 반환하도록 합니다.
    @PostMapping("/api/login")
    @ResponseBody
    public ResponseEntity<LoginResponse> apiLogin(@Valid @RequestBody LoginRequest request) {
        // 이 부분은 API 클라이언트(예: 모바일 앱)를 위한 로그인 로직입니다.
        // 웹 브라우저의 form 로그인은 SecurityConfig의 formLogin()이 처리합니다.
        // 따라서 이 메소드는 당장 사용되지 않을 수 있습니다.
        // ... (기존 로직 생략)
        return ResponseEntity.ok(new LoginResponse("dummy-api-token-for-" + request.email()));
    }

    // [페이지 반환] 로그인 페이지를 보여줍니다.
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // /templates/login.html
    }

    // [페이지 반환] 회원가입 페이지를 보여줍니다.
    @GetMapping("/signup")
    public String signupPage() {
        return "signup"; // /templates/signup.html (새로 만들어야 함)
    }

    // [기능 처리] 회원가입을 처리합니다.
    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute SignUpRequest request) {
        authService.signUp(request);
        // 회원가입 성공 시 로그인 페이지로 리다이렉트
        return "redirect:/auth/login";
    }
}