package com.qring.restaurant.infrastructure.repository;

import com.qring.restaurant.domain.model.RestaurantEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JpaRestaurantRepository extends JpaRepository<RestaurantEntity, Long> {

    // 특정 ID로 삭제되지 않은 식당 존재 여부 확인
    boolean existsByIdAndDeletedAtIsNull(Long id);

    // 특정 ID로 삭제되지 않은 식당과 운영시간 조회
    @Query("SELECT r FROM RestaurantEntity r LEFT JOIN FETCH r.operatingHourEntityList WHERE r.id = :id AND r.deletedAt IS NULL")
    Optional<RestaurantEntity> findByIdWithOperatingHours(@Param("id") Long id);

    // 특정 ID로 삭제되지 않은 식당 조회
    Optional<RestaurantEntity> findByIdAndDeletedAtIsNull(Long id);

    List<Long> findIdByUserIdAndDeletedAtIsNull(Long userId);
}
