// RestaurantRepository.java - 레스토랑 레포지토리 인터페이스
package com.qring.restaurant.domain.repository;

import com.qring.restaurant.domain.model.RestaurantEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface RestaurantRepository {
    // 식당 저장
    RestaurantEntity save(RestaurantEntity restaurantEntity);

    // 특정 ID를 가진 활성 상태(삭제되지 않은)의 식당 조회
    Optional<RestaurantEntity> findByIdAndDeletedAtIsNull(Long id);

    // 조건 기반으로 식당 검색
    Page<RestaurantEntity> findAllByConditions(Long userId, String name, String sort, String address, String category, Pageable pageable);

    // 특정 ID를 가진 활성 상태(삭제되지 않은)의 식당 존재 여부 확인
    boolean existsByIdAndDeletedAtIsNull(Long id);

    // 특정 ID를 가진 식당 엔티티 논리 삭제
    void deleteById(Long id);

    // 식당 정보 업데이트
    RestaurantEntity updateRestaurant(RestaurantEntity restaurantEntity);
}
