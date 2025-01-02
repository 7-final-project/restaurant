package com.qring.restaurant.presentation.v1.controller;

import com.qring.restaurant.application.global.dto.ResDTO;
import com.qring.restaurant.application.v1.res.RestaurantGetByIdResDTOV1;
import com.qring.restaurant.domain.model.constraint.OperationStatus;
import com.qring.restaurant.infrastructure.docs.RestaurantControllerSwagger;
import com.qring.restaurant.presentation.v1.req.PostRestaurantReqDTOV1;
import com.qring.restaurant.application.v1.res.RestaurantSearchResDTOV1;
import com.qring.restaurant.domain.model.CategoryEntity;
import com.qring.restaurant.domain.model.OperatingHourEntity;
import com.qring.restaurant.domain.model.RestaurantEntity;
import com.qring.restaurant.presentation.v1.req.PutRestaurantReqDTOV1;
import com.qring.restaurant.application.v1.res.RestaurantPostResDTOV1;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/v1/restaurants")
public class RestaurantControllerV1 implements RestaurantControllerSwagger {

    @PostMapping
    public ResponseEntity<ResDTO<RestaurantPostResDTOV1>> postBy(@RequestHeader("X-User-Id") Long userId,
                                                                 @Valid @RequestBody PostRestaurantReqDTOV1 dto) {

        // -----
        // TODO : 더미 데이터입니다. 추후 삭제하세요.
        CategoryEntity dummyCategoryEntity = CategoryEntity.builder()
                .name("한식")
                .build();

        List<OperatingHourEntity> dummyOperatingHourList = List.of(
                OperatingHourEntity.builder()
                        .dayOfWeek("월요일")
                        .openAt(LocalTime.parse("09:00"))
                        .closedAt(LocalTime.parse("18:00"))
                        .build(),
                OperatingHourEntity.builder()
                        .dayOfWeek("화요일")
                        .openAt(LocalTime.parse("09:00"))
                        .closedAt(LocalTime.parse("18:00"))
                        .build()
        );

        RestaurantEntity dummyRestaurantEntity = RestaurantEntity.createRestaurantEntity(
                userId,
                "더미 레스토랑",
                50,
                "010-1234-5678",
                "서울특별시 강남구 역삼동",
                "역삼역 근처 1번 출구",
                OperationStatus.OPEN,
                dummyCategoryEntity,
                dummyOperatingHourList,
                "dummyUsername"
        );
        // ----- 추후 삭제하시면 됩니다.

        return new ResponseEntity<>(
                ResDTO.<RestaurantPostResDTOV1>builder()
                        .code(HttpStatus.CREATED.value())
                        .message("식당 생성에 성공하였습니다.")
                        .data(RestaurantPostResDTOV1.of(dummyRestaurantEntity))
                        .build(),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<ResDTO<RestaurantSearchResDTOV1>> searchBy(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                                                     @RequestParam(name = "userId", required = false) Long userId,
                                                                     @RequestParam(name = "name", required = false) String name,
                                                                     @RequestParam(name = "sort", required = false) String sort) {

        // --
        // TODO : 본인 식당 / 식당 이름 / 식당 생성 최신순 / 식당 생성 오래된순 조회
        // FIXME : 평점순으로도 조회할 수 있어야 하지 않을까 ? -> 총 평점은 리뷰가 아니라 식당이 가져야 하는 것이 아닐까 ?
        // --

        // -----
        // TODO : 더미 데이터입니다. 추후 삭제하세요.

        List<RestaurantEntity> dummyRestaurants = List.of(
                RestaurantEntity.builder()
                        .name("왕가탕후루")
                        .tel("123-456-7890")
                        .address("서울시 강남구")
                        .addressDetails("1층")
                        .ratingAverage(4.5)
                        .operationStatus(OperationStatus.OPEN)
                        .categoryEntity(CategoryEntity.builder()
                                .name("한식")
                                .build())
                        .build(),
                RestaurantEntity.builder()
                        .name("맛있는집")
                        .tel("234-567-8901")
                        .address("서울시 송파구")
                        .addressDetails("2층")
                        .ratingAverage(4.0)
                        .operationStatus(OperationStatus.CLOSED)
                        .categoryEntity(CategoryEntity.builder()
                                .name("한식")
                                .build())
                        .build(),
                RestaurantEntity.builder()
                        .name("수미식당")
                        .tel("345-678-9012")
                        .address("서울시 서초구")
                        .addressDetails("3층")
                        .ratingAverage(3.8)
                        .operationStatus(OperationStatus.OPEN)
                        .categoryEntity(CategoryEntity.builder()
                                .name("일식")
                                .build())
                        .build()
        );

        Page<RestaurantEntity> dummyRestaurantPage = new PageImpl<>(dummyRestaurants, pageable, dummyRestaurants.size());
        // ----- 추후 삭제하시면 됩니다.

        return new ResponseEntity<>(
                ResDTO.<RestaurantSearchResDTOV1>builder()
                        .code(HttpStatus.OK.value())
                        .message("식당 검색에 성공하였습니다.")
                        .data(RestaurantSearchResDTOV1.of(dummyRestaurantPage))
                        .build(),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResDTO<RestaurantGetByIdResDTOV1>> getBy(@PathVariable(name = "id") Long id) {

        // -----
        // TODO: 더미 데이터를 반환합니다. 추후 실제 데이터 로직으로 변경하세요.
        List<OperatingHourEntity> dummyOperatingHourList = List.of(
                OperatingHourEntity.builder()
                        .dayOfWeek("월요일")
                        .openAt(LocalTime.parse("09:00"))
                        .closedAt(LocalTime.parse("18:00"))
                        .build(),
                OperatingHourEntity.builder()
                        .dayOfWeek("화요일")
                        .openAt(LocalTime.parse("09:00"))
                        .closedAt(LocalTime.parse("18:00"))
                        .build()
        );

        RestaurantEntity dummyRestaurantEntity = RestaurantEntity.responseRestaurantEntity(
                1L,
                "왕가탕후루",
                50,
                "010-9876-5432",
                "서울특별시 강남구 테헤란로",
                "2층 201호",
                4.5,
                OperationStatus.OPEN,
                CategoryEntity.builder()
                        .name("한식")
                        .build(),
                dummyOperatingHourList,
                "dummyUser"
        );
        // -----

        return new ResponseEntity<>(
                ResDTO.<RestaurantGetByIdResDTOV1>builder()
                        .code(HttpStatus.OK.value())
                        .message("식당 상세 조회에 성공하였습니다.")
                        .data(RestaurantGetByIdResDTOV1.of(dummyRestaurantEntity))
                        .build(),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResDTO<Object>> putBy(@RequestHeader("X-User-Id") Long userId,
                                                @PathVariable(name = "id") Long id,
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


    @DeleteMapping("/{id}")
    public ResponseEntity<ResDTO<Object>> deleteBy(@RequestHeader("X-User-Id") Long userId,
                                                   @PathVariable(name = "id") Long id) {


        return new ResponseEntity<>(
                ResDTO.builder()
                        .code(HttpStatus.OK.value())
                        .message("식당 삭제에 성공하였습니다.")
                        .build(),
                HttpStatus.OK
        );
    }
}
