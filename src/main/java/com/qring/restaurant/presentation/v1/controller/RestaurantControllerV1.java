package com.qring.restaurant.presentation.v1.controller;

import com.qring.restaurant.application.global.dto.ResDTO;
import com.qring.restaurant.application.v1.res.PostRestaurantReqDTOv1;
import com.qring.restaurant.domain.model.CategoryEntity;
import com.qring.restaurant.domain.model.OperatingHourEntity;
import com.qring.restaurant.domain.model.RestaurantEntity;
import com.qring.restaurant.presentation.v1.req.RestaurantPostResDTOv1;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

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
}
