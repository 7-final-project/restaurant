package com.qring.restaurant.presentation.v1.controller;

import com.qring.restaurant.application.global.dto.ResDTO;
import com.qring.restaurant.application.v1.res.CategoryPostResDTOV1;
import com.qring.restaurant.domain.model.CategoryEntity;
import com.qring.restaurant.infrastructure.docs.CategoryControllerSwagger;
import com.qring.restaurant.presentation.v1.req.PostCategoryReqDTOV1;
import com.qring.restaurant.presentation.v1.req.PutCategoryDTOV1;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/restaurants/category")
public class CategoryControllerV1 implements CategoryControllerSwagger {

    @PostMapping
    public ResponseEntity<ResDTO<CategoryPostResDTOV1>> postBy(@RequestHeader("X-User-Id") Long userId,
                                                               @Valid @RequestBody PostCategoryReqDTOV1 dto) {
        // -----
        // TODO : 더미데이터입니다.
        CategoryEntity dummyReviewEntity = CategoryEntity.builder()
                .name("한식")
                .build();
        // ----- 추후 삭제

        return new ResponseEntity<>(
                ResDTO.<CategoryPostResDTOV1>builder()
                        .code(HttpStatus.CREATED.value())
                        .message("카테고리 생성에 성공했습니다.")
                        .data(CategoryPostResDTOV1.of(dummyReviewEntity))
                        .build(),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResDTO<CategoryPostResDTOV1>> putBy(@RequestHeader("X-User-Id") Long userId,
                                                               @PathVariable Long id,
                                                               @Valid @RequestBody PutCategoryDTOV1 dto) {
        // -----
        // TODO : 더미데이터입니다.
        CategoryEntity dummyReviewEntity = CategoryEntity.builder()
                .name("수정 한식")
                .build();
        // ----- 추후 삭제

        return new ResponseEntity<>(
                ResDTO.<CategoryPostResDTOV1>builder()
                        .code(HttpStatus.OK.value())
                        .message("카테고리 수정에 성공했습니다.")
                        .data(CategoryPostResDTOV1.of(dummyReviewEntity))
                        .build(),
                HttpStatus.OK
        );
    }

}
