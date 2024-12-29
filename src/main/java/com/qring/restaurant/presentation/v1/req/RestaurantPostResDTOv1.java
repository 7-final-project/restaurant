package com.qring.restaurant.presentation.v1.req;

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
public class RestaurantPostResDTOv1 {

    private Restaurant restaurant;

    public static RestaurantPostResDTOv1 of(RestaurantEntity restaurantEntity) {
        return RestaurantPostResDTOv1.builder()
                .restaurant(Restaurant.from(restaurantEntity))
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Restaurant {

        private Long userId;
        private Long restaurantId;
        private Long categoryId;
        private String name;
        private String tel;
        private int capacity;
        private String address;
        private String addressDetails;
        private List<OperatingHour> operatingHourList;

        public static Restaurant from(RestaurantEntity restaurantEntity) {
            return Restaurant.builder()
                    .userId(restaurantEntity.getUserId())
                    .restaurantId(restaurantEntity.getId())
                    .categoryId(restaurantEntity.getCategory().getId())
                    .name(restaurantEntity.getName())
                    .tel(restaurantEntity.getTel())
                    .capacity(restaurantEntity.getCapacity())
                    .address(restaurantEntity.getAddress())
                    .addressDetails(restaurantEntity.getAddressDetails())
                    .operatingHourList(OperatingHour.from(restaurantEntity.getOperatingHourEntityList()))
                    .build();
        }


        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class OperatingHour {

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
                        .dayOfWeek(operatingHourEntity.getDayOfWeek())
                        .openAt(operatingHourEntity.getOpenAt())
                        .closedAt(operatingHourEntity.getClosedAt())
                        .build();
            }
        }
    }
}
