package com.study.board.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration  // ✅ 스프링 설정 클래스
public class QuerydslConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean  // ✅ JPAQueryFactory를 스프링 빈으로 등록
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}

