package com.qring.restaurant.application.v1.service;

import com.qring.restaurant.application.global.exception.EntityNotFoundException;
import com.qring.restaurant.application.global.exception.UnauthorizedAccessException;
import com.qring.restaurant.application.v1.res.*;
import com.qring.restaurant.application.v1.scheduler.OperationStatusScheduler;
import com.qring.restaurant.domain.model.CategoryEntity;
import com.qring.restaurant.domain.model.OperatingHourEntity;
import com.qring.restaurant.domain.model.RestaurantEntity;
import com.qring.restaurant.domain.model.constraint.OperationDayOfWeek;
import com.qring.restaurant.domain.model.constraint.OperationStatus;
import com.qring.restaurant.domain.repository.CategoryRepository;
import com.qring.restaurant.domain.repository.RestaurantRepository;
import com.qring.restaurant.infrastructure.util.PassportUtil;
import com.qring.restaurant.presentation.v1.req.PostRestaurantReqDTOV1;
import com.qring.restaurant.presentation.v1.req.PutRestaurantReqDTOV1;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantServiceV1 {

    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;
    private final OperationStatusScheduler operationStatusScheduler;

    // 새로운 식당 생성
    @Transactional
    public RestaurantPostResDTOV1 postBy(String passport, PostRestaurantReqDTOV1 dto) {
        // 1. 유저 권한 검증 (관리자 또는 점주만 생성 가능)
        validateUserRole(PassportUtil.getRole(passport), Set.of("관리자", "점주"));
        // 2. 카테고리 엔티티 조회 (존재하지 않거나 삭제된 카테고리는 예외 처리)
        CategoryEntity category = categoryRepository.findByIdAndDeletedAtIsNull(dto.getRestaurant().getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("카테고리를 찾을 수 없습니다."));
        // 3. 운영 시간 목록 생성 (DTO에서 전달받은 운영 시간을 기반으로 엔티티 생성)
        List<OperatingHourEntity> OperatingHourEntityList = dto.getOperatingHourList().stream()
                .map(hour -> OperatingHourEntity.builder()
                        .operationDayOfWeek(hour.getOperationDayOfWeek())
                        .openAt(hour.getOpenAt())
                        .closedAt(hour.getClosedAt())
                        .build())
                .toList();
        // 4. 초기 운영 상태 결정 (현재 시간 기준으로 OPEN 또는 CLOSED 설정)
        OperationStatus operationStatus = determineOperationStatus(OperatingHourEntityList);
        // 5. 새 식당 엔티티 생성
        RestaurantEntity restaurantEntityForSave = RestaurantEntity.createRestaurantEntity(
                PassportUtil.getUserId(passport),
                dto.getRestaurant().getName(),
                dto.getRestaurant().getCapacity(),
                dto.getRestaurant().getTel(),
                dto.getRestaurant().getAddress(),
                dto.getRestaurant().getAddressDetails(),
                operationStatus,
                category,
                OperatingHourEntityList,
                PassportUtil.getUsername(passport)
        );
        // 6. 식당 엔티티 저장
        restaurantEntityForSave = restaurantRepository.save(restaurantEntityForSave);
        // 7. 스케줄러에 상태 변경 작업 등록
        operationStatusScheduler.scheduleOperationStatusChange(restaurantEntityForSave);
        // 8. 생성된 식당 데이터를 DTO로 반환
        return RestaurantPostResDTOV1.of(restaurantEntityForSave);
    }

    // 조건에 따른 식당 검색
    @Transactional(readOnly = true)
    public RestaurantSearchResDTOV1 searchBy(Long userId, String name, String sort, String address, String category, Pageable pageable) {
        return RestaurantSearchResDTOV1.of(restaurantRepository.findRestaurantPageByDeletedAtIsNullWithConditions(userId, name, sort, address, category, pageable));
    }

    // 식당 상세 조회
    @Transactional(readOnly = true)
    public RestaurantGetByIdResDTOV1 getBy(Long id) {
        RestaurantEntity RestaurantEntityForMapping = restaurantRepository.findByIdWithOperatingHours(id)
                .orElseThrow(() -> new EntityNotFoundException("식당을 찾을 수 없습니다."));
        return RestaurantGetByIdResDTOV1.of(RestaurantEntityForMapping);
    }

    // 식당 수정
    @Transactional
    public void putBy(String passport, Long id, PutRestaurantReqDTOV1 dto) {

        // 1. 유저 권한 검증 (관리자 또는 점주만 수정 가능)
        validateUserRole(PassportUtil.getRole(passport), Set.of("관리자", "점주"));

        // 2. 수정 대상 식당 조회 (존재하지 않거나 삭제된 경우 예외 처리)
        RestaurantEntity existingRestaurant = restaurantRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("식당을 찾을 수 없습니다."));

        // 3. 점주인 경우 본인의 식당인지 확인 (다른 점주의 식당을 수정하려 할 경우 예외 처리)
        validateOwnerRestaurantAccess(passport, existingRestaurant);

        // 4. 카테고리 엔티티 조회 (존재하지 않거나 삭제된 카테고리는 예외 처리)
        CategoryEntity category = categoryRepository.findByIdAndDeletedAtIsNull(dto.getRestaurant().getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("카테고리를 찾을 수 없습니다."));

        // 5. 기존 운영 시간 엔티티를 Map으로 변환 (ID를 기준으로 빠른 조회를 위해 사용)
        Map<Long, OperatingHourEntity> operatingHourEntityMap = existingRestaurant.getOperatingHourEntityList().stream()
                .collect(Collectors.toMap(OperatingHourEntity::getId, hour -> hour));

        // 6. DTO에서 전달받은 운영 시간을 기존 엔티티에 업데이트
        dto.getOperatingHourList().forEach(hour -> {
            OperatingHourEntity operatingHour = operatingHourEntityMap.get(hour.getId());
            if (operatingHour != null) {
                operatingHour.updateOperatingHourEntity(hour.getOperationDayOfWeek(), hour.getOpenAt(), hour.getClosedAt());
            }
        });

        // 7. 운영 상태 재계산 (수정된 운영 시간을 기준으로 OPEN 또는 CLOSED 결정)
        OperationStatus updatedOperationStatus = determineOperationStatus(existingRestaurant.getOperatingHourEntityList());

        // 8. 식당 엔티티 업데이트
        existingRestaurant.updateRestaurantEntity(
                dto.getRestaurant().getName(),
                dto.getRestaurant().getCapacity(),
                dto.getRestaurant().getTel(),
                dto.getRestaurant().getAddress(),
                dto.getRestaurant().getAddressDetails(),
                updatedOperationStatus,
                category,
                PassportUtil.getUsername(passport)
        );

        // 9. 스케줄러에 상태 변경 작업 재등록 (변경된 운영 시간 반영)
        operationStatusScheduler.scheduleOperationStatusChange(existingRestaurant);
    }


    // 식당 삭제
    @Transactional
    public void deleteBy(String passport, Long id) {

        validateUserRole(PassportUtil.getRole(passport), Set.of("관리자", "점주"));

        RestaurantEntity restaurant = restaurantRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("식당을 찾을 수 없습니다."));

        // 점주일 경우 본인의 식당인지 확인
        validateOwnerRestaurantAccess(passport, restaurant);

        // 연관된 운영시간 논리 삭제
        restaurant.getOperatingHourEntityList().forEach(hour -> hour.deleteOperatingHourEntity(PassportUtil.getUsername(passport)));

        // 식당 논리 삭제
        restaurant.deleteRestaurantEntity(PassportUtil.getUsername(passport));
    }

    // 운영 상태 결정
    private OperationStatus determineOperationStatus(List<OperatingHourEntity> OperatingHourEntityList) {
        LocalDateTime now = LocalDateTime.now();
        java.time.DayOfWeek currentDay = now.getDayOfWeek(); // Java 표준 OperationDayOfWeek
        LocalTime currentTime = now.toLocalTime();

        System.out.println("Current Day: " + currentDay);
        System.out.println("Current Time: " + currentTime);

        // 오늘 요일을 우리의 커스텀 DayOfWeek로 변환
        OperationDayOfWeek currentDayInCustomEnum =
                OperationDayOfWeek.valueOf(currentDay.name()); // 커스텀 DayOfWeek로 매핑

        // 오늘 요일의 운영 시간을 순회하며 현재 시간이 범위 내에 있는지 확인
        for (OperatingHourEntity hour : OperatingHourEntityList) {
            System.out.println("Checking day: " + hour.getOperationDayOfWeek() + ", Open: " + hour.getOpenAt() + ", Closed: " + hour.getClosedAt());

            // 현재 요일과 운영 시간 비교
            if (hour.getOperationDayOfWeek().equals(currentDayInCustomEnum.getDescription())) { // description과 비교
                if (!currentTime.isBefore(hour.getOpenAt()) && !currentTime.isAfter(hour.getClosedAt())) {
                    System.out.println("OperationStatus: OPEN");
                    return OperationStatus.OPEN; // 현재 시간이 운영 시간 범위 내에 있으면 OPEN 반환
                }
            }
        }

        System.out.println("OperationStatus: CLOSED");
        return OperationStatus.CLOSED; // 운영 시간이 없거나 현재 시간이 범위 밖이면 CLOSED 반환
    }


    private void validateUserRole(String currentRole, Set<String> requiredRoleSet) {
        if (!requiredRoleSet.contains(currentRole)) {
            throw new UnauthorizedAccessException("접근 권한이 없습니다");
        }
    }

    private void validateOwnerRestaurantAccess(String passport, RestaurantEntity restaurant) {
        // 현재 사용자의 역할 확인
        if (PassportUtil.getRole(passport).equals("점주") &&
                !restaurant.getUserId().equals(PassportUtil.getUserId(passport))) {
            throw new UnauthorizedAccessException("본인의 식당에만 접근할 수 있습니다.");
        }
    }

    @Transactional(readOnly = true)
    public RestaurantExistsByIdResDTOV1 existsBy(Long id) {
        boolean exists = restaurantRepository.existsByIdAndDeletedAtIsNull(id);

        String status = exists ? "exists" : "nonexistence";

        return RestaurantExistsByIdResDTOV1.builder()
                .status(status)
                .build();
    }

    @Transactional(readOnly = true)
    public RestaurantIdTableResDTOV1 getRestaurantTableByUserId(Long userId) {
        List<Long> restaurantIds = restaurantRepository.findRestaurantIdsByUserId(userId);
        return RestaurantIdTableResDTOV1.of(restaurantIds);
    }

}
