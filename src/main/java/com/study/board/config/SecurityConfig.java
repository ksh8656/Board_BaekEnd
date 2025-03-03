package com.study.board.config;

import com.study.board.security.JwtAuthenticationFilter;
import com.study.board.security.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;

@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // ✅ CSRF 비활성화
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))  // ✅ `corsConfigurationSource()` 직접 참조
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user/register", "/user/login", "/user/checkEmail").permitAll()  // ✅ 인증 없이 접근 가능하도록 허용
                        .requestMatchers(HttpMethod.GET, "/board/list").authenticated()
                        .anyRequest().authenticated()  // ✅ 그 외 요청은 인증 필요
                )
                .formLogin(form -> form.disable())  // ✅ 기본 로그인 페이지 비활성화 (REST API용 설정)
                .logout(logout -> logout
                        .logoutSuccessUrl("/login")
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class); // ✅ JWT 필터 추가

        return http.build();
    }

    // 🔥 `corsConfigurationSource()`를 `SecurityConfig` 내부에서 정의
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true); // ✅ 인증 정보 포함 허용
        config.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // ✅ 프론트엔드 허용
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")); // ✅ 모든 HTTP 메서드 허용
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type")); // ✅ 헤더 허용

        source.registerCorsConfiguration("/**", config);
        return source;
    }
}





