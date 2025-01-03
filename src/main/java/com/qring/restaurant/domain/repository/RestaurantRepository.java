package com.qring.restaurant.domain.repository;

import com.qring.restaurant.domain.model.RestaurantEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository {

    // 특정 ID로 삭제되지 않은 식당 존재 여부 확인
    boolean existsByIdAndDeletedAtIsNull(Long id);

    // 특정 ID로 삭제되지 않은 식당 조회
    Optional<RestaurantEntity> findByIdAndDeletedAtIsNull(Long id);

    // 조건에 따른 식당 검색
    Page<RestaurantEntity> findAllByConditions(Long userId, String name, String sort, String address, String category, Pageable pageable);

    // 삭제되지 않은 모든 식당 조회
    List<RestaurantEntity> findAllByDeletedAtIsNull();

    // 식당 저장
    RestaurantEntity save(RestaurantEntity restaurantEntity);

    // 식당 삭제
    void deleteById(Long id);

    // 식당 정보 업데이트
    void updateRestaurant(RestaurantEntity restaurantEntity);
}
