package com.qring.restaurant.infrastructure.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuerydslConfig {

    @PersistenceContext
    private EntityManager entityManager; // EntityManager 주입

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        // JPAQueryFactory 생성 및 반환
        return new JPAQueryFactory(entityManager);
    }
}
