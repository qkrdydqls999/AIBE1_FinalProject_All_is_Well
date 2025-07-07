package org.example.bookmarket.auth.service;

import lombok.RequiredArgsConstructor;
import org.example.bookmarket.auth.dto.SignUpRequest;
import org.example.bookmarket.common.handler.exception.CustomException;
import org.example.bookmarket.common.handler.exception.ErrorCode;
import org.example.bookmarket.user.entity.Role;
import org.example.bookmarket.user.entity.User;
import org.example.bookmarket.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    // [제거] AuthenticationManager, JwtTokenProvider, RedisService 의존성 모두 제거

    // [제거] login 메서드 전체 삭제 (Spring Security가 formLogin으로 자동 처리)

    @Transactional
    public void signUp(SignUpRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }
        if (userRepository.findByNickname(request.nickname()).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
        }

        User newUser = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .nickname(request.nickname())
                .role(Role.USER)
                .profileImageUrl("https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?q=80&w=1780&auto=format&fit=crop")
                .build();
        userRepository.save(newUser);
    }
}