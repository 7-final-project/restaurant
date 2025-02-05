package com.qring.restaurant.infrastructure.repository.hour;

import com.qring.restaurant.domain.model.OperatingHourEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaOperatingHourRepository extends JpaRepository<OperatingHourEntity, Long> {

    List<OperatingHourEntity> findByIdInAndDeletedAtIsNull(List<Long> operatingHourEntityIdList);

}
