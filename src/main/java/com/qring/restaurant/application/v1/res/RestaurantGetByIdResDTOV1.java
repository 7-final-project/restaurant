package com.qring.restaurant.application.v1.res;

import com.qring.restaurant.domain.model.OperatingHourEntity;
import com.qring.restaurant.domain.model.RestaurantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantGetByIdResDTOV1 {

    private Restaurant restaurant;

    public static RestaurantGetByIdResDTOV1 of(RestaurantEntity restaurantEntity) {
        return RestaurantGetByIdResDTOV1.builder()
                .restaurant(Restaurant.from(restaurantEntity))
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Restaurant {

        private Long id;
        private Long categoryId;
        private String name;
        private String tel;
        private String regionCode;
        private String address;
        private String addressDetails;
        private double ratingAverage;
        private String operationStatus;
        private List<OperatingHour> operatingHourList;

        public static Restaurant from(RestaurantEntity restaurantEntity) {
            return Restaurant.builder()
                    .id(restaurantEntity.getId())
                    .categoryId(restaurantEntity.getCategory().getId())
                    .name(restaurantEntity.getName())
                    .tel(restaurantEntity.getTel())
                    .regionCode(restaurantEntity.getRegion().getCode())
                    .address(restaurantEntity.getAddress())
                    .addressDetails(restaurantEntity.getAddressDetails())
                    .ratingAverage(roundToOneDecimalPlace(restaurantEntity.getRatingAverage()))
                    .operationStatus(restaurantEntity.getOperationStatus().getDescription())
                    .operatingHourList(OperatingHour.from(restaurantEntity.getOperatingHourEntityList()))
                    .build();
        }

        // 소수점 한 자리까지 반올림 처리
        private static double roundToOneDecimalPlace(double value) {
            return Math.round(value * 10) / 10.0;
        }

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class OperatingHour {

            private Long id;
            private String operationDayOfWeek;
            private LocalTime openAt;
            private LocalTime closedAt;

            public static List<OperatingHour> from(List<OperatingHourEntity> operatingHourEntityList) {
                return operatingHourEntityList.stream()
                        .map(OperatingHour::from)
                        .toList();
            }

            public static OperatingHour from(OperatingHourEntity operatingHourEntity) {
                return OperatingHour.builder()
                        .id(operatingHourEntity.getId())
                        .operationDayOfWeek(operatingHourEntity.getOperationDayOfWeek())
                        .openAt(operatingHourEntity.getOpenAt())
                        .closedAt(operatingHourEntity.getClosedAt())
                        .build();
            }
        }
    }
}
