package org.example.bookmarket.auth;

import lombok.RequiredArgsConstructor;
import org.example.bookmarket.auth.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomOAuth2UserService customOAuth2UserService;

    // 비밀번호 암호화를 위한 Encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AuthenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(auth -> auth
                        // ▼▼▼ 모든 사람이 접근할 수 있는 경로 설정 ▼▼▼
                        .requestMatchers(
                                "/", "/welcome", "/search","/used-books/**",               // 홈 관련
                                "/auth/login", "/auth/signup","/auth/api/login", // 인증 관련 페이지
                                "/oauth2/**",                   // 소셜 로그인 처리 경로
                                "/swagger-ui/**", "/v3/api-docs/**", // API 문서
                                "/css/**", "/js/**", "/images/**" // 정적 리소스
                        ).permitAll()
                        .anyRequest().authenticated() // 나머지 모든 요청은 인증 필요
                )

                // ▼▼▼ 폼 로그인 설정 ▼▼▼
                .formLogin(form -> form
                        .loginPage("/auth/login")           // 커스텀 로그인 페이지 URL
                        .loginProcessingUrl("/auth/login")  // 로그인 form의 action URL, Spring Security가 처리
                        .defaultSuccessUrl("/", true) // 로그인 성공 시 리다이렉트 될 기본 URL
                        .failureUrl("/auth/login?error=true") // 로그인 실패 시 리다이렉트 될 URL
                )

                // ▼▼▼ 소셜 로그인(OAuth2) 설정 ▼▼▼
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/auth/login") // 로그인 페이지는 동일하게 사용
                        .defaultSuccessUrl("/", true)
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService) // 사용자 정보 처리 서비스
                        )
                )

                // ▼▼▼ 로그아웃 설정 ▼▼▼
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")      // 로그아웃 처리 URL
                        .logoutSuccessUrl("/")      // 로그아웃 성공 시 리다이렉트 될 URL
                )

                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}