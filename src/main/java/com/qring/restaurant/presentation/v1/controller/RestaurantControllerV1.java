package com.qring.restaurant.presentation.v1.controller;

import com.qring.restaurant.application.global.dto.ResDTO;
import com.qring.restaurant.application.v1.res.PostRestaurantReqDTOv1;
import com.qring.restaurant.domain.model.CategoryEntity;
import com.qring.restaurant.domain.model.OperatingHourEntity;
import com.qring.restaurant.domain.model.RestaurantEntity;
import com.qring.restaurant.presentation.v1.req.PutRestaurantReqDTOV1;
import com.qring.restaurant.presentation.v1.req.RestaurantPostResDTOv1;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
public class RestaurantControllerV1 {

    @PostMapping("/v1/restaurants")
    public ResponseEntity<ResDTO<RestaurantPostResDTOv1>> postBy(@RequestHeader("X-User-Id") Long userId,
                                                                 @Valid @RequestBody PostRestaurantReqDTOv1 dto) {

        // -----
        // TODO : 더미 데이터입니다. 추후 삭제하세요.
        CategoryEntity dummyCategoryEntity = CategoryEntity.builder()
                .name("한식")
                .build();

        List<OperatingHourEntity> dummyOperatingHourList = List.of(
                OperatingHourEntity.builder()
                        .dayOfWeek("Monday")
                        .openAt(LocalTime.of(9, 0))
                        .closedAt(LocalTime.of(22, 0))
                        .build(),
                OperatingHourEntity.builder()
                        .dayOfWeek("Tuesday")
                        .openAt(LocalTime.of(9, 0))
                        .closedAt(LocalTime.of(22, 0))
                        .build()
        );

        RestaurantEntity dummyRestaurantEntity = RestaurantEntity.createRestaurantEntity(
                userId,
                "더미 레스토랑",
                50,
                "010-1234-5678",
                "서울특별시 강남구 역삼동",
                "역삼역 근처 1번 출구",
                dummyCategoryEntity,
                dummyOperatingHourList,
                "dummyUsername"
        );
        // ----- 추후 삭제하시면 됩니다.

        return new ResponseEntity<>(
                ResDTO.<RestaurantPostResDTOv1>builder()
                        .code(HttpStatus.CREATED.value())
                        .message("식당 생성에 성공하였습니다.")
                        .data(RestaurantPostResDTOv1.of(dummyRestaurantEntity))
                        .build(),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/v1/restaurants/{restaurantId}")
    public ResponseEntity<ResDTO<Object>> putBy(@RequestHeader("X-User-Id") Long userId,
                                                @PathVariable(name = "restaurantId") Long restaurantId,
                                                @Valid @RequestBody PutRestaurantReqDTOV1 dto) {

        // -----
        /*
            TODO : 식당 수정 비즈니스 로직 가이드 라인
                   step 1. 식당 수정 대상 엔티티 조회
                      - 요청된 restaurantId를 사용하여 해당 식당을 DB 에서 조회합니다.
                      - restaurant 리포지토리에서 findById 같은 걸로 조회하면 되겠죠 ?
                   step 2. 운영 시간 데이터 준비
                      - dto 에서 전달된 운영 시간 ID 목록을 추출하여 리스트로 만듭니다.
                      - ex)  List<Long> operatingHourIdList = dto.getOperatingHourList().stream()
                                          .map(PutRestaurantReqDTOV1.OperatingHour::getId)
                                          .toList();
                  step 3. 운영 시간 엔티티 조회
                      - 앞서 생성한 operatingHourIdList 를 통해 in 절을 이용해서 한 번에 운영 시간 List 를 뽑아옵니다.
                      - ex) findByIdInAndDeletedAtIsNull(List<Long> operatingHourIdList);
                  step 4. 운영 시간 Map 으로 변환
                      - ex)  Map<Long, OperatingHourEntity> operatingHourIdToOperatingHourEntityMap = operatingHourEntityList.stream()
                                      .collect(Collectors.toMap(OperatingHourEntity::getId, OperatingHourEntity -> OperatingHourEntity));
                  step 5. 식당 정보를 업데이트 합니다.
                      - step 1 에서 조회한 식당 정보를 업데이트 합니다.
                      - updateRestaurant 같은 메서드 만들어서 업데이트 해줍니다.
                  step 6. 운영 시간 정보를 업데이트 합니다.
                      - step 4 에서 생성한 map 을 이용하여 업데이트 합니다.
                      - ex)  dto.getOperatingHourList().forEach(operatingHour -> {
                                    OperatingHourEntity operatingHourEntity = operatingHourIdToOperatingHourEntityMap.get(operatingHour.getId());
                                    operatingHourEntity.updateOperatingHourEntity(operatingHour.getDayOfWeek, operatingHour.getOpenAt, operatingHour.getClosedAt);
                               });
        */
        // -----

        return new ResponseEntity<>(
                ResDTO.builder()
                        .code(HttpStatus.OK.value())
                        .message("식당 수정에 성공하였습니다.")
                        .build(),
                HttpStatus.OK
        );
    }
}
