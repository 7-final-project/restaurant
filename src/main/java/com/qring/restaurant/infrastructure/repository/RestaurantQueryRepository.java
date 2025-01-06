package com.qring.restaurant.infrastructure.repository;

import com.qring.restaurant.domain.model.RestaurantEntity;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.qring.restaurant.domain.model.QRestaurantEntity.restaurantEntity;

@Repository
@RequiredArgsConstructor
public class RestaurantQueryRepository {

    private final JPAQueryFactory queryFactory;

    // 조건에 따른 식당 검색
    public Page<RestaurantEntity> findRestaurantPageByDeletedAtIsNullWithConditions(Long userId, String name, String sort, String address, String category, Pageable pageable) {
        var results = queryFactory
                .selectFrom(restaurantEntity)
                .where(
                        userIdEq(userId),
                        categoryIdEq(category),
                        nameLike(name),
                        addressLike(address),
                        restaurantEntity.deletedAt.isNull()
                )
                .orderBy(getOrderSpecifier(sort))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    // 삭제되지 않은 모든 식당 조회
    public List<RestaurantEntity> findAllByDeletedAtIsNull() {
        return queryFactory
                .selectFrom(restaurantEntity)
                .where(restaurantEntity.deletedAt.isNull())
                .fetch();
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
