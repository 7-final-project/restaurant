package com.qring.restaurant.application.v1.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantIdTableResDTOV1 {

    private List<Long> restaurantList;

    public static RestaurantIdTableResDTOV1 of(List<Long> restaurantIdList) {
        return RestaurantIdTableResDTOV1.builder()
                .restaurantList(restaurantIdList)
                .build();
    }

}
