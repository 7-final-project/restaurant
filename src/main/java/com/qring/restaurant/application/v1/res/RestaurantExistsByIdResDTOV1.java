package com.qring.restaurant.application.v1.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantExistsByIdResDTOV1 {

    private String status; // 식당 존재 여부 (exists / nonexistence)
}
