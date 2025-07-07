package org.example.bookmarket.user.service;

import lombok.RequiredArgsConstructor;
import org.example.bookmarket.auth.dto.SignUpRequest;
import org.example.bookmarket.user.entity.User;
import org.example.bookmarket.user.entity.Role; // Role enum을 import 합니다.
import org.example.bookmarket.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 이메일로 사용자를 찾는 기존 메소드
     */
    @Transactional(readOnly = true)
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다: " + email));
    }

    /**
     * 회원가입 처리를 위한 신규 메소드
     */
    @Transactional
    public Long signUp(SignUpRequest request) {
        // 1. 이메일 중복 검사
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 2. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.password());

        // 3. DTO를 User 엔티티로 변환
        User newUser = User.builder()
                .email(request.email())
                .password(encodedPassword)
                .nickname(request.nickname())
                .role(Role.USER)
                .provider("local")
                .build();

        // 4. DB에 사용자 저장
        User savedUser = userRepository.save(newUser);
        return savedUser.getId();
    }
}