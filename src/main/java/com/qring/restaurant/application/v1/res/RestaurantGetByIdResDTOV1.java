package com.qring.restaurant.application.v1.res;

import com.qring.restaurant.domain.model.OperatingHourEntity;
import com.qring.restaurant.domain.model.RestaurantEntity;
import com.qring.restaurant.domain.model.constraint.OperationStatus;
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
        private String address;
        private String addressDetails;
        private Double ratingAverage;
        private OperationStatus operationStatus;
        private List<OperatingHour> operatingHourList;

        public static Restaurant from(RestaurantEntity restaurantEntity) {
            return Restaurant.builder()
                    .id(restaurantEntity.getId())
                    .categoryId(restaurantEntity.getCategory().getId())
                    .name(restaurantEntity.getName())
                    .tel(restaurantEntity.getTel())
                    .address(restaurantEntity.getAddress())
                    .addressDetails(restaurantEntity.getAddressDetails())
                    .ratingAverage(restaurantEntity.getRatingAverage())
                    .operationStatus(restaurantEntity.getOperationStatus())
                    .operatingHourList(OperatingHour.from(restaurantEntity.getOperatingHourEntityList()))
                    .build();
        }

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class OperatingHour {

            private Long id;
            private String dayOfWeek;
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
                        .dayOfWeek(operatingHourEntity.getDayOfWeek())
                        .openAt(operatingHourEntity.getOpenAt())
                        .closedAt(operatingHourEntity.getClosedAt())
                        .build();
            }
        }
    }
}
