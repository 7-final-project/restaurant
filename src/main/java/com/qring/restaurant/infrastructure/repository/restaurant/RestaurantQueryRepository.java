package com.qring.restaurant.infrastructure.repository.restaurant;

import com.qring.restaurant.domain.model.RestaurantEntity;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.qring.restaurant.domain.model.QOperatingHourEntity.operatingHourEntity;
import static com.qring.restaurant.domain.model.QRestaurantEntity.restaurantEntity;

@Repository
@RequiredArgsConstructor
public class RestaurantQueryRepository {

    private final JPAQueryFactory queryFactory;

    // 조건에 따른 식당 검색
    public Page<RestaurantEntity> findRestaurantPageByDeletedAtIsNullWithConditions(
            Long userId, String name, Boolean isOperation, String sort, String address, String category, Pageable pageable) {

        LocalTime now = LocalTime.now();
        DayOfWeek today = LocalDate.now().getDayOfWeek();

        // 조건에 맞는 결과 조회
        List<RestaurantEntity> results = queryFactory
                .selectFrom(restaurantEntity)
                .where(
                        restaurantEntity.deletedAt.isNull(),
                        userIdEq(userId),
                        categoryIdEq(category),
                        nameLike(name),
                        addressLike(address),
                        isOperationEq(isOperation, today, now)
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
                        addressLike(address),
                        isOperationEq(isOperation, today, now)
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

    private BooleanExpression isOperationEq(Boolean isOperation, DayOfWeek today, LocalTime now) {
        if (isOperation == null) {
            return null;
        }

        String operationDayOfWeek = getOperationDayOfWeek(today);

        BooleanExpression isOperating = JPAExpressions
                .selectOne()
                .from(operatingHourEntity)
                .where(
                        operatingHourEntity.restaurantEntity.id.eq(restaurantEntity.id),
                        operatingHourEntity.operationDayOfWeek.eq(operationDayOfWeek),
                        operatingHourEntity.openAt.loe(now),
                        operatingHourEntity.closedAt.gt(now)
                )
                .exists();

        return isOperation ? isOperating : isOperating.not();
    }

    private BooleanExpression nameLike(String name) {
        return name != null ? restaurantEntity.name.containsIgnoreCase(name) : null;
    }

    private BooleanExpression addressLike(String address) {
        return address != null ? restaurantEntity.address.containsIgnoreCase(address) : null;
    }

    private OrderSpecifier<?> getOrderSpecifier(String sort) {
        if (sort == null || sort.isBlank()) {
            return restaurantEntity.createdAt.desc(); // 기본값: 생성일 내림차순 정렬
        }
        switch (sort.toLowerCase()) {
            case "high":
                // 리뷰 평균 점수 높은 순 정렬
                return Expressions.numberTemplate(Double.class,
                        // SQL CASE 구문과 유사: reviewCount > 0이면 totalRating / reviewCount, 아니면 0 반환
                        "case when {0} > 0 then {1} / {0} else 0 end",
                        restaurantEntity.reviewCount,
                        restaurantEntity.totalRating
                ).desc(); // 내림차순 정렬
            case "low":
                // 리뷰 평균 점수 낮은 순 정렬
                return Expressions.numberTemplate(Double.class,
                        "case when {0} > 0 then {1} / {0} else 0 end",
                        restaurantEntity.reviewCount,
                        restaurantEntity.totalRating
                ).asc(); // 오름차순 정렬
            case "oldest":
                // 오래된 순(생성일 오름차순) 정렬
                return restaurantEntity.createdAt.asc();
            default:
                // 기본값: 최신 순(생성일 내림차순) 정렬
                return restaurantEntity.createdAt.desc();
        }
    }

    private String getOperationDayOfWeek(DayOfWeek dayOfWeek) {
        String[] koreanDays = {"월요일", "화요일", "수요일", "목요일", "금요일", "토요일", "일요일"};
        return koreanDays[dayOfWeek.getValue() - 1];
    }
}
