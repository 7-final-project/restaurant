package com.qring.restaurant.infrastructure.scheduler;

import com.qring.restaurant.application.scheduler.OperationStatusScheduler;
import com.qring.restaurant.domain.model.RestaurantEntity;
import com.qring.restaurant.domain.model.constraint.OperationDayOfWeek;
import com.qring.restaurant.domain.model.constraint.OperationStatus;
import com.qring.restaurant.domain.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Component
@RequiredArgsConstructor
public class OperationStatusSchedulerImpl implements OperationStatusScheduler {

    private final TaskScheduler taskScheduler; // TaskScheduler를 이용해 예약 작업을 처리
    private final TransactionTemplate transactionTemplate; // 트랜잭션 관리를 위한 객체
    private final RestaurantRepository restaurantRepository;

    // 예약된 작업들을 관리하기 위한 Map. 레스토랑 ID를 키로 하고 예약된 작업 리스트를 값으로 저장
    private final Map<Long, List<ScheduledFuture<?>>> scheduledTasks = new ConcurrentHashMap<>();

    @Override
    public void scheduleOperationStatusChange(RestaurantEntity restaurant) {
        // 1. 기존 작업 취소
        cancelScheduledTasks(restaurant.getId());

        // 2. 새 작업 리스트 생성
        List<ScheduledFuture<?>> tasks = new ArrayList<>();

        // 3. 레스토랑의 각 운영 시간에 대해 OPEN/CLOSED 작업 예약
        restaurant.getOperatingHourEntityList().forEach(hour -> {
            // OPEN 상태로 변경 작업 예약
            LocalDateTime openTime = calculateNextExecutionTime(hour.getOperationDayOfWeek(), hour.getOpenAt());
            ScheduledFuture<?> openTask = taskScheduler.schedule(() -> {
                System.out.println("Executing OPEN task for restaurant ID: " + restaurant.getId() + " at " + LocalDateTime.now());
                // 트랜잭션 내에서 상태 변경 작업 실행
                executeInTransaction(restaurant, OperationStatus.OPEN);
            }, openTime.atZone(ZoneId.systemDefault()).toInstant());
            tasks.add(openTask);

            // CLOSED 상태로 변경 작업 예약
            LocalDateTime closeTime = calculateNextExecutionTime(hour.getOperationDayOfWeek(), hour.getClosedAt());
            ScheduledFuture<?> closeTask = taskScheduler.schedule(() -> {
                System.out.println("Executing CLOSED task for restaurant ID: " + restaurant.getId() + " at " + LocalDateTime.now());
                // 트랜잭션 내에서 상태 변경 작업 실행
                executeInTransaction(restaurant, OperationStatus.CLOSED);
            }, closeTime.atZone(ZoneId.systemDefault()).toInstant());
            tasks.add(closeTask);
        });

        // 4. 예약된 작업 리스트를 Map에 저장
        scheduledTasks.put(restaurant.getId(), tasks);
        System.out.println("Scheduled tasks for restaurant ID: " + restaurant.getId());
    }

    // 기존 예약 작업 취소
    private void cancelScheduledTasks(Long restaurantId) {
        // 1. Map에서 해당 레스토랑 ID에 해당하는 작업 리스트를 가져옴
        List<ScheduledFuture<?>> tasks = scheduledTasks.remove(restaurantId);
        if (tasks != null) {
            // 2. 작업 리스트에 있는 모든 작업을 취소
            tasks.forEach(task -> task.cancel(false));
        }
    }

    // 다음 실행 시간을 계산
    private LocalDateTime calculateNextExecutionTime(String operationDayOfWeek, LocalTime targetTime) {
        LocalDateTime now = LocalDateTime.now(); // 현재 시간
        java.time.DayOfWeek currentDay = now.getDayOfWeek(); // 현재 요일

        // 1. 한글 요일을 커스텀 OperationDayOfWeek Enum으로 매핑
        OperationDayOfWeek customDay =
                OperationDayOfWeek.fromString(operationDayOfWeek);

        // 2. 커스텀 DayOfWeek를 Java의 DayOfWeek로 변환 (변환하는 이유는 커스텀 OperationDayOfWeek Enum과 Java 표준 OperationDayOfWeek Enum 간의 변환이 필요 )
        java.time.DayOfWeek targetDay = java.time.DayOfWeek.valueOf(customDay.name());

        // 3. 현재 요일에서 목표 요일까지의 차이를 계산
        int daysUntilTarget = targetDay.getValue() - currentDay.getValue();
        if (daysUntilTarget < 0) {
            daysUntilTarget += 7; // 음수일 경우 다음 주로 이동
        }

        // 4. 목표 시간을 포함한 LocalDateTime 객체 생성
        LocalDateTime nextExecutionTime = now.plusDays(daysUntilTarget)
                .withHour(targetTime.getHour())
                .withMinute(targetTime.getMinute())
                .withSecond(0);

        System.out.println("Calculated next execution time for " + operationDayOfWeek + " at " + targetTime + ": " + nextExecutionTime);
        return nextExecutionTime;
    }

    // 트랜잭션 내에서 상태 변경 작업 실행
    private void executeInTransaction(RestaurantEntity restaurant, OperationStatus newStatus) {
        transactionTemplate.execute(status -> {
            // 1. 레스토랑의 운영 상태를 새 상태로 변경
            restaurant.updateOperationStatus(newStatus);

            // 2. 변경된 상태를 DB에 저장
            restaurantRepository.save(restaurant);

            // 3. 상태 변경 로그 출력
            System.out.println("Updated status to: " + newStatus + " for restaurant ID: " + restaurant.getId());
            System.out.println("Current time: " + LocalDateTime.now());
            return null; // 트랜잭션 완료
        });
    }
}
