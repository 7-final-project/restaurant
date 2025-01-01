package com.qring.restaurant.presentation.v1.controller;

import com.qring.restaurant.application.global.dto.ResDTO;
import com.qring.restaurant.application.v1.res.CategoryPostResDTOV1;
import com.qring.restaurant.application.v1.res.CategorySearchResDTOV1;
import com.qring.restaurant.domain.model.CategoryEntity;
import com.qring.restaurant.infrastructure.docs.CategoryControllerSwagger;
import com.qring.restaurant.presentation.v1.req.PostCategoryReqDTOV1;
import com.qring.restaurant.presentation.v1.req.PutCategoryDTOV1;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<ResDTO<CategorySearchResDTOV1>> searchBy(@PageableDefault(sort = "id") Pageable pageable,
                                                                   @RequestParam(name = "name", required = false) String name) {

        // -- TODO : 더미 데이터를 사용한 카테고리 검색. 추후 실제 데이터로 변경
        List<CategoryEntity> dummyCategories = List.of(
                CategoryEntity.builder()
                        .name("한식")
                        .build(),
                CategoryEntity.builder()
                        .name("양식")
                        .build(),
                CategoryEntity.builder()
                        .name("일식")
                        .build()
        );

        Page<CategoryEntity> dummyPage = new PageImpl<>(dummyCategories, pageable, dummyCategories.size());

        // -- 응답 DTO로 변환
        return new ResponseEntity<>(
                ResDTO.<CategorySearchResDTOV1>builder()
                        .code(HttpStatus.OK.value())
                        .message("카테고리 검색에 성공했습니다.")
                        .data(CategorySearchResDTOV1.of(dummyPage))
                        .build(),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResDTO<Object>> putBy(@RequestHeader("X-User-Id") Long userId,
                                                               @PathVariable Long id,
                                                               @Valid @RequestBody PutCategoryDTOV1 dto) {

        return new ResponseEntity<>(
                ResDTO.builder()
                        .code(HttpStatus.OK.value())
                        .message("카테고리 수정에 성공했습니다.")
                        .build(),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResDTO<Object>> deleteBy(@RequestHeader("X-User-Id") Long userId, @PathVariable Long id) {

        return new ResponseEntity<>(
                ResDTO.builder()
                        .code(HttpStatus.OK.value())
                        .message("카테고리 삭제에 성공했습니다.")
                        .build(),
                HttpStatus.OK
        );
    }

}
