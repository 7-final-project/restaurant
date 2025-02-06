package com.qring.restaurant.application.res;

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

    public static RestaurantGetByIdResDTOV1 of(RestaurantEntity restaurantEntity, List<OperatingHourEntity> operatingHourEntityList) {
        return RestaurantGetByIdResDTOV1.builder()
                .restaurant(Restaurant.from(restaurantEntity, operatingHourEntityList))
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
        private double averageRating;
        private List<OperatingHour> operatingHourList;

        public static Restaurant from(RestaurantEntity restaurantEntity, List<OperatingHourEntity> operatingHourEntityList) {
            return Restaurant.builder()
                    .id(restaurantEntity.getId())
                    .categoryId(restaurantEntity.getCategory().getId())
                    .name(restaurantEntity.getName())
                    .tel(restaurantEntity.getTel())
                    .regionCode(restaurantEntity.getRegion().getCode())
                    .address(restaurantEntity.getAddress())
                    .addressDetails(restaurantEntity.getAddressDetails())
                    .averageRating(calculateAverageRating(restaurantEntity))
                    .operatingHourList(OperatingHour.from(operatingHourEntityList))
                    .build();
        }

        private static double calculateAverageRating(RestaurantEntity restaurantEntity) {
            if (restaurantEntity.getReviewCount() == 0) return 0.0;
            return Math.round((double) restaurantEntity.getTotalRating() / restaurantEntity.getReviewCount() * 10) / 10.0;
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
