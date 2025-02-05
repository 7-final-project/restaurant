package com.qring.restaurant.domain.repository;

import com.qring.restaurant.domain.model.OperatingHourEntity;

import java.util.List;

public interface OperatingHourRepository {

    List<OperatingHourEntity> findByIdInAndDeletedAtIsNull(List<Long> operatingHourEntityIdList);
}
