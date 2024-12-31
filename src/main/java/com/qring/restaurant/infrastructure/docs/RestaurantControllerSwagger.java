package com.qring.restaurant.infrastructure.docs;


import com.qring.restaurant.application.global.dto.ResDTO;
import com.qring.restaurant.application.v1.res.RestaurantGetByIdResDTOV1;
import com.qring.restaurant.application.v1.res.RestaurantPostResDTOV1;
import com.qring.restaurant.application.v1.res.RestaurantSearchResDTOV1;
import com.qring.restaurant.presentation.v1.req.PostRestaurantReqDTOV1;
import com.qring.restaurant.presentation.v1.req.PutRestaurantReqDTOV1;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Review", description = "생성, 조회, 검색, 수정, 삭제 관련 리뷰 API")
@RequestMapping("/v1/restaurants")
public interface RestaurantControllerSwagger {

    @Operation(summary = "식당 생성", description = "식당을 생성하는 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "식당 생성 성공", content = @Content(schema = @Schema(implementation = ResDTO.class))),
            @ApiResponse(responseCode = "400", description = "식상 생성 실패.", content = @Content(schema = @Schema(implementation = ResDTO.class)))
    })
    @PostMapping
    ResponseEntity<ResDTO<RestaurantPostResDTOV1>> postBy(@RequestHeader("X-User-Id") Long userId, @Valid @RequestBody PostRestaurantReqDTOV1 dto);


    @Operation(summary = "식당 검색", description = "식당을 검색하는 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "식당 검색 성공", content = @Content(schema = @Schema(implementation = ResDTO.class))),
            @ApiResponse(responseCode = "400", description = "식상 검색 실패.", content = @Content(schema = @Schema(implementation = ResDTO.class)))
    })
    @GetMapping
    ResponseEntity<ResDTO<RestaurantSearchResDTOV1>> searchBy(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                                              @RequestParam(name = "userId", required = false) Long userId,
                                                              @RequestParam(name = "name", required = false) String name,
                                                              @RequestParam(name = "sort", required = false) String sort);


    @Operation(summary = "식당 상세 조회", description = "식당을 상세 조회하는 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "식당 상세 조회 성공", content = @Content(schema = @Schema(implementation = ResDTO.class))),
            @ApiResponse(responseCode = "400", description = "식당 상세 조회 실패.", content = @Content(schema = @Schema(implementation = ResDTO.class)))
    })
    @GetMapping("/{id}")
    ResponseEntity<ResDTO<RestaurantGetByIdResDTOV1>> getBy(@PathVariable(name = "id") Long id);


    @Operation(summary = "식당 수정", description = "식당을 수정하는 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "식당 수정 성공", content = @Content(schema = @Schema(implementation = ResDTO.class))),
            @ApiResponse(responseCode = "400", description = "식당 수정 실패.", content = @Content(schema = @Schema(implementation = ResDTO.class)))
    })
    @PutMapping("/{id}")
    ResponseEntity<ResDTO<Object>> putBy(@RequestHeader("X-User-Id") Long userId, @PathVariable(name = "id") Long id, @Valid @RequestBody PutRestaurantReqDTOV1 dto);


    @Operation(summary = "식당 삭제", description = "식당을 삭제하는 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "식당 삭제 성공", content = @Content(schema = @Schema(implementation = ResDTO.class))),
            @ApiResponse(responseCode = "400", description = "식당 삭제 실패.", content = @Content(schema = @Schema(implementation = ResDTO.class)))
    })
    @DeleteMapping("/{id}")
    ResponseEntity<ResDTO<Object>> deleteBy(@RequestHeader("X-User-Id") Long userId, @PathVariable(name = "id") Long id);
}
