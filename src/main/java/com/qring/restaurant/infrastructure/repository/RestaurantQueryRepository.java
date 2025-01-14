package com.qring.restaurant.infrastructure.repository;

import com.qring.restaurant.domain.model.RestaurantEntity;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.qring.restaurant.domain.model.QRestaurantEntity.restaurantEntity;

@Repository
@RequiredArgsConstructor
public class RestaurantQueryRepository {

    private final JPAQueryFactory queryFactory;

    // 조건에 따른 식당 검색
    public Page<RestaurantEntity> findRestaurantPageByDeletedAtIsNullWithConditions(
            Long userId, String name, String sort, String address, String category, Pageable pageable) {

        // 조건에 맞는 결과 조회
        List<RestaurantEntity> results = queryFactory
                .selectFrom(restaurantEntity)
                .where(
                        restaurantEntity.deletedAt.isNull(),
                        userIdEq(userId),
                        categoryIdEq(category),
                        nameLike(name),
                        addressLike(address)
                )
                .orderBy(getOrderSpecifier(sort))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 총 개수 조회
        JPQLQuery<Long> countQuery = queryFactory
                .select(restaurantEntity.count())
                .from(restaurantEntity)
                .where(
                        restaurantEntity.deletedAt.isNull(),
                        userIdEq(userId),
                        categoryIdEq(category),
                        nameLike(name),
                        addressLike(address)
                );

        // Page 반환
        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
    }


    // 조건 메서드들
    private BooleanExpression userIdEq(Long userId) {
        return userId != null ? restaurantEntity.userId.eq(userId) : null;
    }

    private BooleanExpression categoryIdEq(String category) {
        return category != null ? restaurantEntity.category.name.eq(category) : null;
    }

    private BooleanExpression nameLike(String name) {
        return name != null ? restaurantEntity.name.containsIgnoreCase(name) : null;
    }

    private BooleanExpression addressLike(String address) {
        return address != null ? restaurantEntity.address.containsIgnoreCase(address) : null;
    }


    private OrderSpecifier<?> getOrderSpecifier(String sort) {
        if(sort == null || sort.isBlank()){
            return restaurantEntity.createdAt.desc(); // 기본값
        }
        switch (sort.toLowerCase()) {
            case "high":
                return restaurantEntity.ratingAverage.desc();
            case "low":
                return restaurantEntity.ratingAverage.asc();
            case "oldest":
                return restaurantEntity.createdAt.asc();
            default:
                return restaurantEntity.createdAt.desc();
        }
    }
}
