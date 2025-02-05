package com.qring.restaurant.infrastructure.repository;

import com.qring.restaurant.domain.model.OperatingHourEntity;
import com.qring.restaurant.domain.repository.OperatingHourRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OperatingHourRepositoryImpl implements OperatingHourRepository {

    private final JpaOperatingHourRepository jpaOperatingHourRepository;

    @Override
    public List<OperatingHourEntity> findByIdInAndDeletedAtIsNull(List<Long> operatingHourEntityIdList) {
        return jpaOperatingHourRepository.findByIdInAndDeletedAtIsNull(operatingHourEntityIdList);
    }
}
