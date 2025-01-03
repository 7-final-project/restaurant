package com.qring.restaurant.infrastructure.repository;

import com.qring.restaurant.domain.model.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaRestaurantRepository extends JpaRepository<RestaurantEntity, Long> {

    // 특정 ID로 삭제되지 않은 식당 존재 여부 확인
    boolean existsByIdAndDeletedAtIsNull(Long id);

    // 특정 ID로 삭제되지 않은 식당 조회
    Optional<RestaurantEntity> findByIdAndDeletedAtIsNull(Long id);

    // 삭제되지 않은 모든 식당 조회
    List<RestaurantEntity> findAllByDeletedAtIsNull();
}
