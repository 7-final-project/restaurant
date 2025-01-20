package com.qring.restaurant.application.v1.res;

import com.qring.restaurant.domain.model.OperatingHourEntity;
import com.qring.restaurant.domain.model.RestaurantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Comparator;
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
        private String area;
        private String address;
        private String addressDetails;
        private double averageRating;
        private String operationStatus;
        private List<OperatingHour> operatingHourList;

        public static Restaurant from(RestaurantEntity restaurantEntity) {
            return Restaurant.builder()
                    .id(restaurantEntity.getId())
                    .categoryId(restaurantEntity.getCategory().getId())
                    .name(restaurantEntity.getName())
                    .tel(restaurantEntity.getTel())
                    .area(restaurantEntity.getArea())
                    .address(restaurantEntity.getAddress())
                    .addressDetails(restaurantEntity.getAddressDetails())
                    .averageRating(calculateAverageRating(restaurantEntity)) // 평균 평점 계산
                    .operationStatus(restaurantEntity.getOperationStatus().getDescription())
                    .operatingHourList(OperatingHour.from(restaurantEntity.getOperatingHourEntityList()))
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
                // 각 요일 정렬 기준을 만듦
                List<String> dayOfWeekOrder = List.of("월요일", "화요일", "수요일", "목요일", "금요일", "토요일", "일요일");

                // 정렬 후 변환
                return operatingHourEntityList.stream()
                        // Comparator.comparingInt를 사용하여 요일 순서대로 정렬
                        .sorted(Comparator.comparingInt(
                                entity -> {
                                    // 현재 요일을 dayOfWeekOrder 리스트에서 검색하여 인덱스를 가져옴
                                    int index = dayOfWeekOrder.indexOf(entity.getOperationDayOfWeek());
                                    // dayOfWeekOrder에 없는 요일은 -1을 반환하므로 Integer.MAX_VALUE로 처리하여 가장 뒤로 보냄
                                    return index == -1 ? Integer.MAX_VALUE : index;
                                }
                        ))
                        // 정렬된 결과를 OperatingHour 객체로 변환
                        .map(OperatingHour::from)
                        .toList(); // 리스트로 변환하여 반환
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
