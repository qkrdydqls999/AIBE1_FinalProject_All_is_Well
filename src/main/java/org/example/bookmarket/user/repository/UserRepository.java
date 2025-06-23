package org.example.bookmarket.user.repository;

import org.example.bookmarket.user.entity.SocialType;
import org.example.bookmarket.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 이메일로 사용자를 찾는 메서드 (로그인, 중복 체크 시 사용)
    Optional<User> findByEmail(String email);

    // 닉네임으로 사용자를 찾는 메서드 (중복 체크 시 사용)
    Optional<User> findByNickname(String nickname);

    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);
}