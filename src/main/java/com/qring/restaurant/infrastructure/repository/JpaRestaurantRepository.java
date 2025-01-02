// JpaRestaurantRepository.java - JPA 기반 기본 레포지토리
package com.qring.restaurant.infrastructure.repository;

import com.qring.restaurant.domain.model.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaRestaurantRepository extends JpaRepository<RestaurantEntity, Long> {
    // 특정 ID를 가진 활성 상태(삭제되지 않은)의 식당 조회
    Optional<RestaurantEntity> findByIdAndDeletedAtIsNull(Long id);

    // 특정 ID를 가진 활성 상태(삭제되지 않은)의 식당 존재 여부 확인
    boolean existsByIdAndDeletedAtIsNull(Long id);
}
