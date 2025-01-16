package com.qring.restaurant.domain.repository;

import com.qring.restaurant.domain.model.RegionEntity;

import java.util.Optional;

public interface RegionRepository {

    // 특정 ID로 삭제되지 않은 지역 조회
    Optional<RegionEntity> findByIdAndDeletedAtIsNull(Long id);

    // 지역 저장
    RegionEntity save(RegionEntity regionEntity);
}
