package com.qring.restaurant.presentation.v1.controller;

import com.qring.restaurant.application.global.dto.ResDTO;
import com.qring.restaurant.application.v1.res.CategoryGetByIdResDTOV1;
import com.qring.restaurant.application.v1.res.CategoryPostResDTOV1;
import com.qring.restaurant.application.v1.res.CategoryTableGetResDTOV1;
import com.qring.restaurant.application.v1.service.CategoryServiceV1;
import com.qring.restaurant.infrastructure.docs.CategoryControllerSwagger;
import com.qring.restaurant.presentation.v1.req.PostCategoryReqDTOV1;
import com.qring.restaurant.presentation.v1.req.PutCategoryDTOV1;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/restaurants/category")
@RequiredArgsConstructor
public class CategoryControllerV1 implements CategoryControllerSwagger {

    private final CategoryServiceV1 categoryServiceV1;

    @PostMapping
    public ResponseEntity<ResDTO<CategoryPostResDTOV1>> postBy(@RequestHeader("X-Passport-Token") String passport,
                                                               @Valid @RequestBody PostCategoryReqDTOV1 dto) {
        return new ResponseEntity<>(
                ResDTO.<CategoryPostResDTOV1>builder()
                        .code(HttpStatus.CREATED.value())
                        .message("카테고리 생성에 성공했습니다.")
                        .data(categoryServiceV1.postBy(passport, dto))
                        .build(),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<ResDTO<CategoryTableGetResDTOV1>> search() {
        return new ResponseEntity<>(
                ResDTO.<CategoryTableGetResDTOV1>builder()
                        .code(HttpStatus.OK.value())
                        .message("카테고리 검색에 성공했습니다.")
                        .data(categoryServiceV1.search())
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
                        .data(categoryServiceV1.getBy(id))
                        .build(),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResDTO<Object>> putBy(@RequestHeader("X-Passport-Token") String passport,
                                                @PathVariable Long id,
                                                @Valid @RequestBody PutCategoryDTOV1 dto) {
        categoryServiceV1.putBy(passport, id, dto);

        return new ResponseEntity<>(
                ResDTO.builder()
                        .code(HttpStatus.OK.value())
                        .message("카테고리 수정에 성공했습니다.")
                        .build(),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResDTO<Object>> deleteBy(@RequestHeader("X-Passport-Token") String passport,
                                                   @PathVariable Long id) {
        categoryServiceV1.deleteBy(passport, id);

        return new ResponseEntity<>(
                ResDTO.builder()
                        .code(HttpStatus.OK.value())
                        .message("카테고리 삭제에 성공했습니다.")
                        .build(),
                HttpStatus.OK
        );
    }
}
