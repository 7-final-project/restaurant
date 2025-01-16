package com.qring.restaurant.domain.repository;

import com.qring.restaurant.domain.model.RegionEntity;

import java.util.Optional;

public interface RegionRepository {

    // 특정 ID로 삭제되지 않은 지역 조회
    Optional<RegionEntity> findByIdAndDeletedAtIsNull(Long id);

    Optional<RegionEntity> findByNameAndDeletedAtIsNull(String name);

    // 지역 저장
    RegionEntity save(RegionEntity regionEntity);

    // 특정 이름으로 삭제되지 않은 지역 존재 여부 확인
    boolean existsByNameAndDeletedAtIsNull(String name);

}
