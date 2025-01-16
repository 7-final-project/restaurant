package com.qring.restaurant.infrastructure.repository;

import com.qring.restaurant.domain.model.RegionEntity;
import com.qring.restaurant.domain.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RegionRepositoryImpl implements RegionRepository {

    private final JpaRegionRepository jpaRegionRepository;

    @Override
    public Optional<RegionEntity> findByIdAndDeletedAtIsNull(Long id) {
        return jpaRegionRepository.findByIdAndDeletedAtIsNull(id);
    }

    @Override
    public RegionEntity save(RegionEntity regionEntity) {
        return jpaRegionRepository.save(regionEntity);
    }
}
