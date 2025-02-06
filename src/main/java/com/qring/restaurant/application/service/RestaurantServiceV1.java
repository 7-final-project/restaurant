package com.qring.restaurant.application.service;

import com.qring.restaurant.application.global.exception.EntityNotFoundException;
import com.qring.restaurant.application.global.exception.UnauthorizedAccessException;
import com.qring.restaurant.application.res.RestaurantGetByIdResDTOV1;
import com.qring.restaurant.application.res.RestaurantIdTableResDTOV1;
import com.qring.restaurant.application.res.RestaurantPostResDTOV1;
import com.qring.restaurant.application.res.RestaurantSearchResDTOV1;
import com.qring.restaurant.domain.model.CategoryEntity;
import com.qring.restaurant.domain.model.OperatingHourEntity;
import com.qring.restaurant.domain.model.RegionEntity;
import com.qring.restaurant.domain.model.RestaurantEntity;
import com.qring.restaurant.domain.repository.CategoryRepository;
import com.qring.restaurant.domain.repository.OperatingHourRepository;
import com.qring.restaurant.domain.repository.RegionRepository;
import com.qring.restaurant.domain.repository.RestaurantRepository;
import com.qring.restaurant.infrastructure.util.PassportUtil;
import com.qring.restaurant.presentation.req.PostRestaurantReqDTOV1;
import com.qring.restaurant.presentation.req.PutRestaurantReqDTOV1;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RestaurantServiceV1 {

    private final RestaurantRepository restaurantRepository;
    private final OperatingHourRepository operatingHourRepository;
    private final CategoryRepository categoryRepository;
    private final RegionRepository regionRepository;

    // 새로운 식당 생성
    @Transactional
    public RestaurantPostResDTOV1 postBy(String passport, PostRestaurantReqDTOV1 dto) {

        // 1. 유저 권한 검증
        validateUserRole(PassportUtil.getRole(passport), Set.of("관리자", "점주"));

        // 2. 카테고리 엔티티 조회
        CategoryEntity categoryEntity = getCategoryById(dto.getRestaurant().getCategoryId());

        // 3. 운영 시간 목록 생성
        List<OperatingHourEntity> operatingHourEntityList = getOperatingHourEntityList(passport, dto);

        // 4. 지역 조회
        RegionEntity regionEntity = getRegionByRegionCodeStr(dto.getRestaurant().getRegionCode());

        // 5. 새 식당 엔티티 생성
        RestaurantEntity restaurantEntityForSave = RestaurantEntity.createRestaurantEntity(
                PassportUtil.getUserId(passport),
                dto.getRestaurant().getName(),
                dto.getRestaurant().getCapacity(),
                dto.getRestaurant().getTel(),
                regionEntity,
                dto.getRestaurant().getAddress(),
                dto.getRestaurant().getAddressDetails(),
                categoryEntity,
                operatingHourEntityList,
                PassportUtil.getUsername(passport)
        );

        // 6. 식당 엔티티 저장
        restaurantRepository.save(restaurantEntityForSave);

        // 8. 생성된 식당 데이터를 DTO로 반환
        return RestaurantPostResDTOV1.of(restaurantEntityForSave);
    }

    // 조건에 따른 식당 검색
    @Transactional(readOnly = true)
    public RestaurantSearchResDTOV1 searchBy(Long userId, String name, Boolean isOperation, String sort, String address, String category, Pageable pageable) {
        return RestaurantSearchResDTOV1.of(restaurantRepository.findRestaurantPageByDeletedAtIsNullWithConditions(userId, name, isOperation, sort, address, category, pageable));
    }

    // 식당 상세 조회
    @Transactional(readOnly = true)
    public RestaurantGetByIdResDTOV1 getBy(Long id) {

        RestaurantEntity restaurantEntityForMapping = getRestaurantById(id);

        List<OperatingHourEntity> operatingHourEntityListForMapping = operatingHourRepository.findByIdInAndDeletedAtIsNull(
                getOperatingHourEntityIdListFromRestaurantEntity(restaurantEntityForMapping)
        );

        return RestaurantGetByIdResDTOV1.of(restaurantEntityForMapping, operatingHourEntityListForMapping);
    }

    // 식당 수정
    @Transactional
    public void putBy(String passport, Long id, PutRestaurantReqDTOV1 dto) {

        // 1. 유저 권한 검증
        validateUserRole(PassportUtil.getRole(passport), Set.of("관리자", "점주"));

        // 2. 식당 조회
        RestaurantEntity existingRestaurant = getRestaurantById(id);

        // 3. 본인 식당 검증
        validateOwnerRestaurantAccess(passport, existingRestaurant);

        // 4. 카테고리 조회
        CategoryEntity category = getCategoryById(dto.getRestaurant().getCategoryId());

        // 5. 지역 조회
        RegionEntity region = getRegionByRegionCodeStr(dto.getRestaurant().getRegionCode());

        // 6. 식당 엔티티 업데이트
        existingRestaurant.updateRestaurantEntity(
                dto.getRestaurant().getName(),
                dto.getRestaurant().getCapacity(),
                dto.getRestaurant().getTel(),
                dto.getRestaurant().getAddress(),
                region,
                dto.getRestaurant().getAddressDetails(),
                category,
                PassportUtil.getUsername(passport)
        );
    }

    // 식당 삭제
    @Transactional
    public void deleteBy(String passport, Long id) {

        validateUserRole(PassportUtil.getRole(passport), Set.of("관리자", "점주"));

        RestaurantEntity restaurant = getRestaurantById(id);

        // 점주일 경우 본인의 식당인지 확인
        validateOwnerRestaurantAccess(passport, restaurant);

        // 연관된 운영시간 논리 삭제
        restaurant.getOperatingHourEntityList().forEach(hour -> hour.deleteOperatingHourEntity(PassportUtil.getUsername(passport)));

        // 식당 논리 삭제
        restaurant.deleteRestaurantEntity(PassportUtil.getUsername(passport));
    }

    @Transactional(readOnly = true)
    public RestaurantIdTableResDTOV1 getBy(String passport) {
        List<Long> restaurantIdList = restaurantRepository.findIdListByUserIdAndDeletedAtIsNull(PassportUtil.getUserId(passport));
        return RestaurantIdTableResDTOV1.of(restaurantIdList);
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

    private RestaurantEntity getRestaurantById(Long id) {
        return restaurantRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("식당을 찾을 수 없습니다."));
    }

    private CategoryEntity getCategoryById(Long categoryId) {
        return categoryRepository.findByIdAndDeletedAtIsNull(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("카테고리를 찾을 수 없습니다."));
    }

    private RegionEntity getRegionByRegionCodeStr(String regionCode) {
        return regionRepository.findByCodeAndDeletedAtIsNull(regionCode)
                .orElseThrow(() -> new EntityNotFoundException("지역을 찾을 수 없습니다."));
    }

    private static List<Long> getOperatingHourEntityIdListFromRestaurantEntity(RestaurantEntity restaurantEntityForMapping) {
        return restaurantEntityForMapping.getOperatingHourEntityList().stream()
                .map(OperatingHourEntity::getId)
                .toList();
    }

    private static List<OperatingHourEntity> getOperatingHourEntityList(String passport, PostRestaurantReqDTOV1 dto) {
        return dto.getOperatingHourList()
                .stream().map(hour -> OperatingHourEntity.createOperatingHourEntity(
                        hour.getOperationDayOfWeek(),
                        hour.getOpenAt(),
                        hour.getClosedAt(),
                        PassportUtil.getUsername(passport)
                ))
                .toList();
    }
}
