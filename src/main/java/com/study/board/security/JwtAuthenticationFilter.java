package com.study.board.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import jakarta.servlet.FilterChain; // ✅ 최신 Spring Boot 3.x 사용 시
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final SecretKey key;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        this.key = jwtUtil.getKey();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String token = parseJwt(request);

        if (token == null) {
            System.out.println("🚨 JWT 토큰이 요청에 포함되지 않음");
            chain.doFilter(request, response);
            return;
        }

        if (!jwtUtil.validateToken(token)) {
            System.out.println("🚨 JWT 토큰 검증 실패");
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "JWT 검증 실패");
            return;
        }

        String email = jwtUtil.extractEmail(token);
        System.out.println("✅ JwtAuthenticationFilter - 추출된 이메일: " + email);

        if (email != null) {
            UserDetails userDetails = new User(email, "", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);

        System.out.println("SecurityContext Authentication: " + SecurityContextHolder.getContext().getAuthentication());

    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }

}


