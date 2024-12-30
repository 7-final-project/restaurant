package com.qring.restaurant.presentation.v1.req;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

import java.time.LocalTime;
import java.util.List;

@Getter
public class PutRestaurantReqDTOV1 {

    @Valid
    @NotNull(message = "식당 정보를 입력해주세요.")
    private Restaurant restaurant;

    @Valid
    @NotNull(message = "식당 운영 시간을 입력해주세요.")
    List<OperatingHour> operatingHourList;

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

        @NotBlank(message = "주소를 입력해주세요")
        private String address;

        private String addressDetails;

    }

    @Getter
    public static class OperatingHour {

        @NotNull(message = "운영시간 ID 를 입력해주세요")
        private Long id;

        @NotNull(message = "요일을 입력해주세요")
        private String dayOfWeek;

        @NotNull(message = "오픈 시간을 입력해주세요")
        private LocalTime openAt;

        @NotNull(message = "마감 시간을 입력해주세요")
        private LocalTime closedAt;

    }
}

