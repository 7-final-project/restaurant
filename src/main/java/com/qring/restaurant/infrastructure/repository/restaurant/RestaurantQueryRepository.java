package com.qring.restaurant.infrastructure.repository.restaurant;

import com.qring.restaurant.domain.model.RestaurantEntity;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.qring.restaurant.domain.model.QOperatingHourEntity.operatingHourEntity;
import static com.qring.restaurant.domain.model.QRestaurantEntity.restaurantEntity;

@Repository
@RequiredArgsConstructor
public class RestaurantQueryRepository {

    private final JPAQueryFactory queryFactory;

    // 커서 기반 페이지네이션 적용
    public Slice<RestaurantEntity> findRestaurantPageByDeletedAtIsNullWithConditions(
            Long userId, String name, Boolean isOperation, String sort, String address, String category, Long cursor, int limit) {

        LocalTime now = LocalTime.now();
        DayOfWeek today = LocalDate.now().getDayOfWeek();

        List<RestaurantEntity> results = queryFactory
                .selectFrom(restaurantEntity)
                .join(restaurantEntity.region).fetchJoin()
                .join(restaurantEntity.category).fetchJoin()
                .where(
                        restaurantEntity.deletedAt.isNull(),
                        userIdEq(userId),
                        categoryIdEq(category),
                        nameLike(name),
                        addressLike(address),
                        isOperationEq(isOperation, today, now),
                        cursorPagination(cursor, sort)
                )
                .orderBy(getOrderSpecifiers(sort).toArray(new OrderSpecifier<?>[0]))
                .limit(limit + 1) // 다음 페이지 존재 여부 확인을 위해 limit보다 1개 더 조회
                .fetch();

        // 다음 페이지 존재 여부 확인
        boolean hasNext = results.size() > limit;
        if (hasNext) {
            results.remove(results.size() - 1); // 초과한 1개 제거
        }

        return new SliceImpl<>(results, PageRequest.of(0, limit), hasNext);
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

    private BooleanExpression cursorPagination(Long cursor, String sort) {
        if (cursor == null) {
            return null;
        }

        boolean isOldest = sort != null && sort.toLowerCase().contains("oldest");

        return isOldest ? restaurantEntity.id.goe(cursor) : restaurantEntity.id.loe(cursor);
    }

    private List<OrderSpecifier<?>> getOrderSpecifiers(String sort) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();

        boolean isOldest = sort != null && sort.toLowerCase().contains("oldest");

        if (sort != null) {
            if (sort.toLowerCase().contains("high")) {
                // 평점 높은 순 정렬
                orders.add(Expressions.numberTemplate(Double.class,
                        "case when {0} > 0 then {1} / {0} else 0 end",
                        restaurantEntity.reviewCount,
                        restaurantEntity.totalRating
                ).desc().nullsLast());
            } else if (sort.toLowerCase().contains("low")) {
                // 평점 낮은 순 정렬
                orders.add(Expressions.numberTemplate(Double.class,
                        "case when {0} > 0 then {1} / {0} else 0 end",
                        restaurantEntity.reviewCount,
                        restaurantEntity.totalRating
                ).asc().nullsLast());
            }
        }

        // 최신순 또는 오래된순 정렬 적용
        orders.add(isOldest ? restaurantEntity.id.asc() : restaurantEntity.id.desc());

        return orders;
    }

    private String getOperationDayOfWeek(DayOfWeek dayOfWeek) {
        String[] koreanDays = {"월요일", "화요일", "수요일", "목요일", "금요일", "토요일", "일요일"};
        return koreanDays[dayOfWeek.getValue() - 1];
    }
}
