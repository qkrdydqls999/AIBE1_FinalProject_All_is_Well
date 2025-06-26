package org.example.bookmarket.auth.service;

import lombok.RequiredArgsConstructor;
import org.example.bookmarket.user.entity.Role;
import org.example.bookmarket.user.entity.SocialType;
import org.example.bookmarket.user.entity.User;
import org.example.bookmarket.user.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 카카오 로그인 정보 추출
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        String socialId = attributes.get("id").toString();
        String nickname = profile.get("nickname").toString();

        // DB에서 기존 회원인지, 신규 회원인지 판단
        Optional<User> findUser = userRepository.findBySocialTypeAndSocialId(SocialType.KAKAO, socialId);

        User user;
        if (findUser.isEmpty()) {
            // 신규 회원이면 자동 회원가입
            user = User.builder()
                    .nickname(nickname)
                    // 소셜 로그인 사용자의 이메일은 선택 동의 항목이라 없을 수 있으므로, 임시 이메일을 생성합니다.
                    .email(socialId + "@kakao.com")
                    .password(UUID.randomUUID().toString())
                    .role(Role.USER)
                    .socialType(SocialType.KAKAO)
                    .socialId(socialId)
                    .build();
            userRepository.save(user);
        } else {
            user = findUser.get();
        }

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())),
                attributes,
                userNameAttributeName
        );
    }
}