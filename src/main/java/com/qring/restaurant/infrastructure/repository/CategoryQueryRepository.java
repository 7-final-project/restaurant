package com.qring.restaurant.infrastructure.repository;

import com.qring.restaurant.domain.model.CategoryEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.qring.restaurant.domain.model.QCategoryEntity.categoryEntity;

@Repository
@RequiredArgsConstructor
public class CategoryQueryRepository {

    private final JPAQueryFactory queryFactory;

    // 삭제되지 않은 모든 카테고리 조회
    public List<CategoryEntity> findAllByDeletedAtIsNull() {
        return queryFactory
                .selectFrom(categoryEntity)
                .where(categoryEntity.deletedAt.isNull())
                .fetch();
    }
}
