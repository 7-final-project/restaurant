package com.qring.restaurant.infrastructure.docs;

import com.qring.restaurant.application.global.dto.ResDTO;
import com.qring.restaurant.application.v1.res.CategoryPostResDTOV1;
import com.qring.restaurant.application.v1.res.CategorySearchResDTOV1;
import com.qring.restaurant.presentation.v1.req.PostCategoryReqDTOV1;
import com.qring.restaurant.presentation.v1.req.PutCategoryDTOV1;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Tag(name = "Category", description = "생성, 조회, 검색, 수정, 삭제 관련 카테고리 API")
@RequestMapping("/v1/restaurants/category")
public interface CategoryControllerSwagger {

    @Operation(summary = "카테고리 생성", description = "카테고리를 생성하는 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "카테고리 생성 성공", content = @Content(schema = @Schema(implementation = ResDTO.class))),
            @ApiResponse(responseCode = "400", description = "카테고리 생성 실패.", content = @Content(schema = @Schema(implementation = ResDTO.class)))
    })
    @PostMapping
    ResponseEntity<ResDTO<CategoryPostResDTOV1>> postBy(@RequestHeader("X-User-Id") Long userId, @Valid @RequestBody PostCategoryReqDTOV1 dto);

    @Operation(summary = "카테고리 검색", description = "카테고리를 검색하는 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카테고리 검색 성공", content = @Content(schema = @Schema(implementation = ResDTO.class))),
            @ApiResponse(responseCode = "400", description = "카테고리 검색 실패.", content = @Content(schema = @Schema(implementation = ResDTO.class)))
    })
    @GetMapping("/v1/reviews")
    ResponseEntity<ResDTO<CategorySearchResDTOV1>> searchBy(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                                            @RequestParam(name = "sort", required = false) String sort);



    @Operation(summary = "카테고리 수정", description = "카테고리를 수정하는 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카테고리 수정 성공", content = @Content(schema = @Schema(implementation = ResDTO.class))),
            @ApiResponse(responseCode = "400", description = "카테고리 수정 실패.", content = @Content(schema = @Schema(implementation = ResDTO.class)))
    })
    @PutMapping("/{id}")
    ResponseEntity<ResDTO<Object>> putBy(@RequestHeader("X-User-Id") Long userId,
                                         @PathVariable Long id,
                                         @Valid @RequestBody PutCategoryDTOV1 dto);


    @Operation(summary = "카테고리 삭제", description = "카테고리를 삭제하는 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰 삭제 성공", content = @Content(schema = @Schema(implementation = ResDTO.class))),
            @ApiResponse(responseCode = "400", description = "리뷰 삭제 실패.", content = @Content(schema = @Schema(implementation = ResDTO.class)))
    })
    @DeleteMapping("/{id}")
    ResponseEntity<ResDTO<Object>> deleteBy(@RequestHeader("X-User-Id") Long userId, @PathVariable Long id);



}
