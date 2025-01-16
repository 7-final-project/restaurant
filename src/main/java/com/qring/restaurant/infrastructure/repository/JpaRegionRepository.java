package com.qring.restaurant.infrastructure.repository;

import com.qring.restaurant.domain.model.RegionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaRegionRepository extends JpaRepository<RegionEntity, Long> {

    // 특정 ID로 삭제되지 않은 지역 조회
    Optional<RegionEntity> findByIdAndDeletedAtIsNull(Long id);
}
