package com.qring.restaurant.presentation.v1.controller;

import com.qring.restaurant.application.global.dto.ResDTO;
import com.qring.restaurant.application.v1.res.CategoryPostResDTOV1;
import com.qring.restaurant.domain.model.CategoryEntity;
import com.qring.restaurant.presentation.v1.req.PostCategoryReqDTOV1;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/restaurants/category")
public class CategoryControllerV1 {

    @PostMapping
    public ResponseEntity<ResDTO<CategoryPostResDTOV1>> postBy(@RequestHeader("X-User-Id") Long userId,
                                                               @Valid @RequestBody PostCategoryReqDTOV1 dto) {
        // -----
        // TODO : 더미데이터입니다.
        CategoryEntity dummyReviewEntity = CategoryEntity.builder()
                .name("한식")
                .build();
        // ----- 추후 삭제하시면 됩니다.

        return new ResponseEntity<>(
                ResDTO.<CategoryPostResDTOV1>builder()
                        .code(HttpStatus.CREATED.value())
                        .message("카테고리 생성에 성공했습니다.")
                        .data(CategoryPostResDTOV1.of(dummyReviewEntity))
                        .build(),
                HttpStatus.CREATED
        );
    }
}
