package com.qring.restaurant.infrastructure.repository;

import com.qring.restaurant.domain.model.RestaurantEntity;
import com.qring.restaurant.domain.repository.RestaurantRepository;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.qring.restaurant.domain.model.QRestaurantEntity.restaurantEntity;

@Repository
@RequiredArgsConstructor
public class RestaurantRepositoryImpl implements RestaurantRepository {

    private final JpaRestaurantRepository jpaRestaurantRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public RestaurantEntity save(RestaurantEntity restaurantEntity) {
        // JPA를 통해 엔티티 저장
        return jpaRestaurantRepository.save(restaurantEntity);
    }

    @Override
    public Optional<RestaurantEntity> findByIdAndDeletedAtIsNull(Long id) {
        // 특정 ID의 활성 상태(삭제되지 않은) 엔티티 조회
        return jpaRestaurantRepository.findByIdAndDeletedAtIsNull(id);
    }

    @Override
    public Page<RestaurantEntity> findAllByConditions(Long userId, String name, String sort, String address, String category, Pageable pageable) {
        // QueryDSL로 동적 쿼리를 구성하여 검색 조건에 맞는 결과 반환
        QueryResults<RestaurantEntity> results = queryFactory
                .selectFrom(restaurantEntity)
                .where(
                        userIdEq(userId), // 사용자 ID 조건
                        nameContains(name), // 이름 검색 조건
                        addressContains(address), // 주소 검색 조건
                        categoryContains(category), // 카테고리 검색 조건
                        restaurantEntity.deletedAt.isNull() // 삭제되지 않은 엔티티
                )
                .orderBy(getOrderSpecifier(sort)) // 정렬 조건
                .offset(pageable.getOffset()) // 페이지 시작
                .limit(pageable.getPageSize()) // 페이지 크기
                .fetchResults(); // 결과 조회

        // QueryResults를 Page 객체로 변환하여 반환
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    @Override
    public boolean existsByIdAndDeletedAtIsNull(Long id) {
        // 특정 ID의 활성 상태 여부 확인
        return jpaRestaurantRepository.existsByIdAndDeletedAtIsNull(id);
    }

    @Override
    public void deleteById(Long id) {
        // 논리 삭제 - 삭제된 시간 설정
        Optional<RestaurantEntity> restaurant = jpaRestaurantRepository.findByIdAndDeletedAtIsNull(id);
        restaurant.ifPresent(r -> {
            r.setDeletedAt(LocalDateTime.now());
            jpaRestaurantRepository.save(r);
        });
    }

    @Override
    public RestaurantEntity updateRestaurant(RestaurantEntity restaurantEntity) {
        // 레스토랑 수정 - 엔티티를 업데이트하여 저장
        return jpaRestaurantRepository.save(restaurantEntity);
    }

    // 조건 메서드들
    private BooleanExpression userIdEq(Long userId) {
        return userId != null ? restaurantEntity.userId.eq(userId) : null;
    }

    private BooleanExpression nameContains(String name) {
        return name != null ? restaurantEntity.name.containsIgnoreCase(name) : null;
    }

    private BooleanExpression addressContains(String address) {
        return address != null ? restaurantEntity.address.containsIgnoreCase(address) : null;
    }

    private BooleanExpression categoryContains(String category) {
        return category != null ? restaurantEntity.category.name.containsIgnoreCase(category) : null;
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
