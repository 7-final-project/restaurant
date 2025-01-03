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
        restaurant.setDeletedAt(LocalDateTime.now());
        restaurant.setDeletedBy(String.valueOf(userId));
        restaurantRepository.updateRestaurant(restaurant);
    }

    // 운영 상태 결정
    private OperationStatus determineOperationStatus(List<OperatingHourEntity> operatingHours) {
        LocalDateTime now = LocalDateTime.now();
        String currentDay = now.getDayOfWeek().toString();
        LocalTime currentTime = now.toLocalTime();

        return operatingHours.stream()
                .filter(hour -> hour.getDayOfWeek().equalsIgnoreCase(currentDay))
                .anyMatch(hour -> currentTime.isAfter(hour.getOpenAt()) && currentTime.isBefore(hour.getClosedAt()))
                ? OperationStatus.OPEN
                : OperationStatus.CLOSED;
    }
}
