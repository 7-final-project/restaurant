package com.qring.restaurant.presentation.req;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PutRestaurantReqDTOV1 {

    @Valid
    @NotNull(message = "식당 정보를 입력해주세요.")
    private Restaurant restaurant;

    @Getter
    public static class Restaurant {

        @NotNull(message = "카테고리를 입력해주세요")
        private Long categoryId;

        @NotBlank(message = "식당 이름을 입력해주세요")
        private String name;

        @NotBlank(message = "전화번호를 입력해주세요")
        private String tel;

        @Positive(message = "수용 테이블 수를 입력해주세요")
        private int capacity;

        @NotBlank(message = "지역 코드를 입력해주세요")
        private String regionCode;

        @NotBlank(message = "주소를 입력해주세요")
        private String address;

        private String addressDetails;

    }
}

