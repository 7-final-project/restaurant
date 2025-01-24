package com.qring.restaurant.application.scheduler;

import com.qring.restaurant.domain.model.RestaurantEntity;

public interface OperationStatusScheduler {
    void scheduleOperationStatusChange(RestaurantEntity restaurant); // 상태 변경 작업 예약
}
