package com.qring.restaurant.presentation.controller;

import com.qring.restaurant.application.global.dto.ResDTO;
import com.qring.restaurant.application.res.RestaurantGetByIdResDTOV1;
import com.qring.restaurant.application.res.RestaurantIdTableResDTOV1;
import com.qring.restaurant.application.res.RestaurantPostResDTOV1;
import com.qring.restaurant.application.res.RestaurantSearchResDTOV1;
import com.qring.restaurant.application.service.RestaurantServiceV1;
import com.qring.restaurant.infrastructure.docs.RestaurantControllerSwagger;
import com.qring.restaurant.presentation.req.PostRestaurantReqDTOV1;
import com.qring.restaurant.presentation.req.PutRestaurantReqDTOV1;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/restaurants")
@RequiredArgsConstructor
public class RestaurantControllerV1 implements RestaurantControllerSwagger {

    private final RestaurantServiceV1 restaurantServiceV1;

    // 식당 생성
    @PostMapping
    public ResponseEntity<ResDTO<RestaurantPostResDTOV1>> postBy(@RequestHeader("X-Passport-Token") String passport,
                                                                 @Valid @RequestBody PostRestaurantReqDTOV1 dto) {
        return new ResponseEntity<>(
                ResDTO.<RestaurantPostResDTOV1>builder()
                        .code(HttpStatus.CREATED.value())
                        .message("식당 생성에 성공하였습니다.")
                        .data(restaurantServiceV1.postBy(passport, dto))
                        .build(),
                HttpStatus.CREATED
        );
    }

    // 식당 검색
    @GetMapping
    public ResponseEntity<ResDTO<RestaurantSearchResDTOV1>> searchBy(Pageable pageable,
                                                                     @RequestParam(name = "userId", required = false) Long userId,
                                                                     @RequestParam(name = "name", required = false) String name,
                                                                     @RequestParam(name = "isOperation", required = false) Boolean isOperation,
                                                                     @RequestParam(name = "sort", required = false) String sort,
                                                                     @RequestParam(name = "address", required = false) String address,
                                                                     @RequestParam(name = "category", required = false) String category) {
        return new ResponseEntity<>(
                ResDTO.<RestaurantSearchResDTOV1>builder()
                        .code(HttpStatus.OK.value())
                        .message("식당 검색에 성공하였습니다.")
                        .data(restaurantServiceV1.searchBy(userId, name, isOperation, sort, address, category, pageable))
                        .build(),
                HttpStatus.OK
        );
    }

    // 식당 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<ResDTO<RestaurantGetByIdResDTOV1>> getBy(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(
                ResDTO.<RestaurantGetByIdResDTOV1>builder()
                        .code(HttpStatus.OK.value())
                        .message("식당 상세 조회에 성공하였습니다.")
                        .data(restaurantServiceV1.getBy(id))
                        .build(),
                HttpStatus.OK
        );
    }

    // 식당 수정
    @PutMapping("/{id}")
    public ResponseEntity<ResDTO<Object>> putBy(@RequestHeader("X-Passport-Token") String passport,
                                                @PathVariable(name = "id") Long id,
                                                @Valid @RequestBody PutRestaurantReqDTOV1 dto) {
        restaurantServiceV1.putBy(passport, id, dto);
        return new ResponseEntity<>(
                ResDTO.builder()
                        .code(HttpStatus.OK.value())
                        .message("식당 수정에 성공하였습니다.")
                        .build(),
                HttpStatus.OK
        );
    }

    // 식당 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<ResDTO<Object>> deleteBy(@RequestHeader("X-Passport-Token") String passport,
                                                   @PathVariable(name = "id") Long id) {
        restaurantServiceV1.deleteBy(passport, id);
        return new ResponseEntity<>(
                ResDTO.builder()
                        .code(HttpStatus.OK.value())
                        .message("식당 삭제에 성공하였습니다.")
                        .build(),
                HttpStatus.OK
        );
    }

    @GetMapping("/my")
    public ResponseEntity<ResDTO<RestaurantIdTableResDTOV1>> getBy(@RequestHeader("X-Passport-Token") String passport) {
        return new ResponseEntity<>(
                ResDTO.<RestaurantIdTableResDTOV1>builder()
                        .code(HttpStatus.OK.value())
                        .message("사용자의 식당 아이디 리스트를 가져왔습니다.")
                        .data(restaurantServiceV1.getBy(passport))
                        .build(),
                HttpStatus.OK
        );
    }
}
