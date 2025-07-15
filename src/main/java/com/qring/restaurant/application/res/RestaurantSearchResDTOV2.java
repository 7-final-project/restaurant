package com.qring.restaurant.application.res;

import com.qring.restaurant.domain.model.RestaurantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantSearchResDTOV2 {
    private RestaurantPage restaurantPage;

    public static RestaurantSearchResDTOV2 of(Slice<RestaurantEntity> restaurantEntitySlice) {
        return RestaurantSearchResDTOV2.builder()
                .restaurantPage(RestaurantPage.from(restaurantEntitySlice))
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RestaurantPage {

        private List<Restaurant> restaurantList;
        private PageDetails page;

        public static RestaurantPage from(Slice<RestaurantEntity> restaurantEntitySlice) {
            return RestaurantPage.builder()
                    .restaurantList(Restaurant.from(restaurantEntitySlice.getContent()))
                    .page(PageDetails.from(restaurantEntitySlice))
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

            public static List<Restaurant> from(List<RestaurantEntity> restaurantEntityList) {
                return restaurantEntityList.stream()
                        .map(Restaurant::from)
                        .toList();
            }

            public static Restaurant from(RestaurantEntity restaurantEntity) {
                return Restaurant.builder()
                        .id(restaurantEntity.getId())
                        .categoryId(restaurantEntity.getCategory().getId())
                        .name(restaurantEntity.getName())
                        .tel(restaurantEntity.getTel())
                        .regionCode(restaurantEntity.getRegion().getCode())
                        .address(restaurantEntity.getAddress())
                        .addressDetails(restaurantEntity.getAddressDetails())
                        .averageRating(calculateAverageRating(restaurantEntity)) // 평균 평점 계산
                        .build();
            }

            private static double calculateAverageRating(RestaurantEntity restaurantEntity) {
                if (restaurantEntity.getReviewCount() == 0) return 0.0;
                return Math.round((double) restaurantEntity.getTotalRating() / restaurantEntity.getReviewCount() * 10) / 10.0;
            }
        }

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class PageDetails {

            private int size;
            private int number;
            private boolean hasNext; // ✅ Slice 에서 `totalPages` 대신 사용

            public static PageDetails from(Slice<RestaurantEntity> restaurantEntitySlice) {
                return PageDetails.builder()
                        .size(restaurantEntitySlice.getSize())
                        .number(restaurantEntitySlice.getNumber())
                        .hasNext(restaurantEntitySlice.hasNext()) // ✅ `hasNext` 필드 추가
                        .build();
            }
        }
    }
}
