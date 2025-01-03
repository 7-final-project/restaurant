package com.qring.restaurant.application.v1.res;

import com.qring.restaurant.domain.model.RestaurantEntity;
import com.qring.restaurant.domain.model.constraint.OperationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantSearchResDTOV1 {

    private RestaurantPage restaurantPage;

    public static RestaurantSearchResDTOV1 of(Page<RestaurantEntity> restaurantEntityPage) {
        return RestaurantSearchResDTOV1.builder()
                .restaurantPage(RestaurantPage.from(restaurantEntityPage))
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RestaurantPage {

        private List<Restaurant> restaurantList;
        private PageDetails page;

        public static RestaurantPage from(Page<RestaurantEntity> restaurantEntityPage) {
            return RestaurantPage.builder()
                    .restaurantList(Restaurant.from(restaurantEntityPage.getContent()))
                    .page(PageDetails.from(restaurantEntityPage))
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
                        .address(restaurantEntity.getAddress())
                        .addressDetails(restaurantEntity.getAddressDetails())
                        .ratingAverage(restaurantEntity.getRatingAverage())
                        .operationStatus(restaurantEntity.getOperationStatus())
                        .build();
            }
        }


        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class PageDetails {

            private int size;
            private int number;
            private long totalElements;
            private int totalPages;

            public static PageDetails from(Page<RestaurantEntity> restaurantEntityPage) {
                return PageDetails.builder()
                        .size(restaurantEntityPage.getSize())
                        .number(restaurantEntityPage.getNumber())
                        .totalElements(restaurantEntityPage.getTotalElements())
                        .totalPages(restaurantEntityPage.getTotalPages())
                        .build();
            }
        }
    }
}
