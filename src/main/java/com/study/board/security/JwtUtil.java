package com.study.board.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    private final MacAlgorithm algorithm = Jwts.SIG.HS256; // ✅ 최신 버전 호환

    @Getter
    private final SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode("w/oN6ZxhlV2BhzrMz1X3sO8KpjEdbD0H+M5Uv30P+DA=")); // ✅ SecretKey 명확하게 지정
    private final long expirationMs = 1000 * 60 * 60; // 1시간 (3600000ms)

    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key, algorithm) // ✅ MacAlgorithm 명확하게 지정
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            System.out.println("📢 validateToken() 실행됨. 토큰: " + token);

            Jwts.parser()
                    .verifyWith(key) // ✅ 최신 방식 사용
                    .build()
                    .parseSignedClaims(token);

            System.out.println("✅ JWT 검증 성공");
            return true;
        } catch (Exception e) {
            System.out.println("🚨 JWT 검증 실패: " + e.getMessage()); // ✅ JWT 검증 실패 로그 추가
            return false;
        }
    }


    public String extractEmail(String token) {
        try {
            Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
            System.out.println("JwtUtil - 토큰에서 추출된 이메일: " + claims.getSubject()); // ✅ 로그 확인
            return claims.getSubject();
        } catch (Exception e) {
            System.out.println("JwtUtil - 토큰 검증 실패: " + e.getMessage());
            return null; // 토큰이 유효하지 않으면 null 반환
        }
    }


}

