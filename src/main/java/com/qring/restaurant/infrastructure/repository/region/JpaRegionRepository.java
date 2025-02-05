package com.qring.restaurant.infrastructure.repository.region;

import com.qring.restaurant.domain.model.RegionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaRegionRepository extends JpaRepository<RegionEntity, Long> {

    // 특정 코드로 삭제되지 않은 지역 조회
    Optional<RegionEntity> findByCodeAndDeletedAtIsNull(String code);

    // 특정 이름으로 삭제되지 않은 지역 조회
    Optional<RegionEntity> findByNameAndDeletedAtIsNull(String name);

    // 특정 이름으로 삭제되지 않은 지역 존재 여부 확인
    boolean existsByNameAndDeletedAtIsNull(String name);
}
