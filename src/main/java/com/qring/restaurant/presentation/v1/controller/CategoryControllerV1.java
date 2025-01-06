package com.qring.restaurant.presentation.v1.controller;

import com.qring.restaurant.application.global.dto.ResDTO;
import com.qring.restaurant.application.v1.res.CategoryGetByIdResDTOV1;
import com.qring.restaurant.application.v1.res.CategoryPostResDTOV1;
import com.qring.restaurant.application.v1.res.CategorySearchResDTOV1;
import com.qring.restaurant.application.v1.service.CategoryService;
import com.qring.restaurant.domain.model.CategoryEntity;
import com.qring.restaurant.infrastructure.docs.CategoryControllerSwagger;
import com.qring.restaurant.presentation.v1.req.PostCategoryReqDTOV1;
import com.qring.restaurant.presentation.v1.req.PutCategoryDTOV1;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/restaurants/category")
@RequiredArgsConstructor
public class CategoryControllerV1 implements CategoryControllerSwagger{

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<ResDTO<CategoryPostResDTOV1>> postBy(@RequestHeader("X-User-Id") Long userId,
                                                               @Valid @RequestBody PostCategoryReqDTOV1 dto) {
        return new ResponseEntity<>(
                ResDTO.<CategoryPostResDTOV1>builder()
                        .code(HttpStatus.CREATED.value())
                        .message("카테고리 생성에 성공했습니다.")
                        .data(CategoryPostResDTOV1.of(categoryService.postBy(userId, dto)))
                        .build(),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<ResDTO<CategorySearchResDTOV1>> search() {
        return new ResponseEntity<>(
                ResDTO.<CategorySearchResDTOV1>builder()
                        .code(HttpStatus.OK.value())
                        .message("카테고리 검색에 성공했습니다.")
                        .data(CategorySearchResDTOV1.of(categoryService.search()))
                        .build(),
                HttpStatus.OK
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<ResDTO<CategoryGetByIdResDTOV1>> getBy(@PathVariable Long id) {
        return new ResponseEntity<>(
                ResDTO.<CategoryGetByIdResDTOV1>builder()
                        .code(HttpStatus.OK.value())
                        .message("카테고리 상세 조회에 성공했습니다.")
                        .data(CategoryGetByIdResDTOV1.of(categoryService.getBy(id)))
                        .build(),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResDTO<Object>> putBy(@RequestHeader("X-User-Id") Long userId,
                                                @PathVariable Long id,
                                                @Valid @RequestBody PutCategoryDTOV1 dto) {
        categoryService.putBy(userId, id, dto);

        return new ResponseEntity<>(
                ResDTO.builder()
                        .code(HttpStatus.OK.value())
                        .message("카테고리 수정에 성공했습니다.")
                        .build(),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResDTO<Object>> deleteBy(@RequestHeader("X-User-Id") Long userId,
                                                   @PathVariable Long id) {
        categoryService.deleteBy(userId, id);

        return new ResponseEntity<>(
                ResDTO.builder()
                        .code(HttpStatus.OK.value())
                        .message("카테고리 삭제에 성공했습니다.")
                        .build(),
                HttpStatus.OK
        );
    }
}
