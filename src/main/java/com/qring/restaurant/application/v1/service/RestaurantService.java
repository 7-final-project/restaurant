package com.qring.restaurant.application.v1.service;

import com.qring.restaurant.application.global.exception.EntityNotFoundException;
import com.qring.restaurant.application.v1.res.RestaurantGetByIdResDTOV1;
import com.qring.restaurant.application.v1.res.RestaurantPostResDTOV1;
import com.qring.restaurant.application.v1.res.RestaurantSearchResDTOV1;
import com.qring.restaurant.domain.model.CategoryEntity;
import com.qring.restaurant.domain.model.OperatingHourEntity;
import com.qring.restaurant.domain.model.RestaurantEntity;
import com.qring.restaurant.domain.model.constraint.OperationStatus;
import com.qring.restaurant.domain.repository.CategoryRepository;
import com.qring.restaurant.domain.repository.RestaurantRepository;
import com.qring.restaurant.presentation.v1.req.PostRestaurantReqDTOV1;
import com.qring.restaurant.presentation.v1.req.PutRestaurantReqDTOV1;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;

    // 새로운 식당 생성
    public RestaurantPostResDTOV1 createRestaurant(Long userId, PostRestaurantReqDTOV1 dto) {
        CategoryEntity category = categoryRepository.findByIdAndDeletedAtIsNull(dto.getRestaurant().getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("카테고리를 찾을 수 없습니다."));

        List<OperatingHourEntity> operatingHours = dto.getOperatingHourList().stream()
                .map(hour -> OperatingHourEntity.builder()
                        .dayOfWeek(hour.getDayOfWeek())
                        .openAt(hour.getOpenAt())
                        .closedAt(hour.getClosedAt())
                        .build())
                .toList();

        OperationStatus operationStatus = determineOperationStatus(operatingHours);

        RestaurantEntity restaurant = RestaurantEntity.createRestaurantEntity(
                userId,
                dto.getRestaurant().getName(),
                dto.getRestaurant().getCapacity(),
                dto.getRestaurant().getTel(),
                dto.getRestaurant().getAddress(),
                dto.getRestaurant().getAddressDetails(),
                operationStatus,
                category,
                operatingHours,
                String.valueOf(userId)
        );
        restaurant = restaurantRepository.save(restaurant);
        return RestaurantPostResDTOV1.of(restaurant); // DTO로 변환
    }

    // 조건에 따른 식당 검색
    public RestaurantSearchResDTOV1 searchRestaurants(Long userId, String name, String sort, String address, String category, Pageable pageable) {
        Page<RestaurantEntity> restaurantEntities = restaurantRepository.findAllByConditions(userId, name, sort, address, category, pageable);
        return RestaurantSearchResDTOV1.of(restaurantEntities); // DTO로 변환
    }

    // 식당 상세 조회
    public RestaurantGetByIdResDTOV1 getRestaurantById(Long id) {
        RestaurantEntity restaurant = restaurantRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("식당을 찾을 수 없습니다."));
        return RestaurantGetByIdResDTOV1.of(restaurant); // DTO로 변환
    }

    // 식당 수정
    public void updateRestaurant(Long userId, Long id, PutRestaurantReqDTOV1 dto) {
        RestaurantEntity existingRestaurant = restaurantRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("식당을 찾을 수 없습니다."));

        CategoryEntity category = categoryRepository.findByIdAndDeletedAtIsNull(dto.getRestaurant().getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("카테고리를 찾을 수 없습니다."));

        Map<Long, OperatingHourEntity> operatingHourMap = existingRestaurant.getOperatingHourEntityList().stream()
                .collect(Collectors.toMap(OperatingHourEntity::getId, hour -> hour));

        dto.getOperatingHourList().forEach(hour -> {
            OperatingHourEntity operatingHour = operatingHourMap.get(hour.getId());
            if (operatingHour != null) {
                operatingHour.updateOperatingHourEntity(hour.getDayOfWeek(), hour.getOpenAt(), hour.getClosedAt());
            }
        });

        OperationStatus updatedOperationStatus = determineOperationStatus(existingRestaurant.getOperatingHourEntityList());

        existingRestaurant.updateRestaurantEntity(
                dto.getRestaurant().getName(),
                dto.getRestaurant().getCapacity(),
                dto.getRestaurant().getTel(),
                dto.getRestaurant().getAddress(),
                dto.getRestaurant().getAddressDetails(),
                updatedOperationStatus,
                category
        );

        restaurantRepository.updateRestaurant(existingRestaurant);
    }

    // 식당 삭제
    public void deleteRestaurant(Long userId, Long id) {
        RestaurantEntity restaurant = restaurantRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("식당을 찾을 수 없습니다."));
        // 연관된 운영시간 논리 삭제
        restaurant.getOperatingHourEntityList().forEach(hour -> hour.deleteOperatingHourEntity(String.valueOf(userId)));

        // 식당 논리 삭제
        restaurant.setDeletedAt(LocalDateTime.now());
        restaurant.setDeletedBy(String.valueOf(userId));
        restaurantRepository.updateRestaurant(restaurant);
    }

    // 운영 상태 결정
    private OperationStatus determineOperationStatus(List<OperatingHourEntity> operatingHours) {
        LocalDateTime now = LocalDateTime.now();
        java.time.DayOfWeek currentDay = now.getDayOfWeek(); // Java 표준 DayOfWeek
        LocalTime currentTime = now.toLocalTime();

        System.out.println("Current Day: " + currentDay);
        System.out.println("Current Time: " + currentTime);

        // 오늘 요일을 우리의 커스텀 DayOfWeek로 변환
        com.qring.restaurant.domain.model.constraint.DayOfWeek currentDayInCustomEnum =
                com.qring.restaurant.domain.model.constraint.DayOfWeek.valueOf(currentDay.name()); // 커스텀 DayOfWeek로 매핑

        // 오늘 요일의 운영 시간을 순회하며 현재 시간이 범위 내에 있는지 확인
        for (OperatingHourEntity hour : operatingHours) {
            System.out.println("Checking day: " + hour.getDayOfWeek() + ", Open: " + hour.getOpenAt() + ", Closed: " + hour.getClosedAt());

            // 현재 요일과 운영 시간 비교
            if (hour.getDayOfWeek().equals(currentDayInCustomEnum.getDescription())) { // description과 비교
                if (!currentTime.isBefore(hour.getOpenAt()) && !currentTime.isAfter(hour.getClosedAt())) {
                    System.out.println("OperationStatus: OPEN");
                    return OperationStatus.OPEN; // 현재 시간이 운영 시간 범위 내에 있으면 OPEN 반환
                }
            }
        }

        System.out.println("OperationStatus: CLOSED");
        return OperationStatus.CLOSED; // 운영 시간이 없거나 현재 시간이 범위 밖이면 CLOSED 반환
    }

}
