package com.qring.restaurant.infrastructure.docs;

import com.qring.restaurant.application.global.dto.ResDTO;
import com.qring.restaurant.application.v1.res.CategoryPostResDTOV1;
import com.qring.restaurant.presentation.v1.req.PostCategoryReqDTOV1;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Category", description = "카테고리 관련 API")
@RequestMapping("/v1/restaurants/category")
public interface CategoryControllerSwagger {

    @Operation(summary = "카테고리 생성", description = "카테고리를 생성하는 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "카테고리 생성 성공", content = @Content(schema = @Schema(implementation = ResDTO.class))),
            @ApiResponse(responseCode = "400", description = "카테고리 생성 실패.", content = @Content(schema = @Schema(implementation = ResDTO.class)))
    })
    @PostMapping
    ResponseEntity<ResDTO<CategoryPostResDTOV1>> postBy(@RequestHeader("X-User-Id") Long userId, @Valid @RequestBody PostCategoryReqDTOV1 dto);
}
