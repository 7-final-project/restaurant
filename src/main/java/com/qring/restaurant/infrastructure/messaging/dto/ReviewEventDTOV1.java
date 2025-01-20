package com.qring.restaurant.infrastructure.messaging.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewEventDTOV1 {

    private Long restaurantId;
    private int rating;
    private String eventType; // "CREATE", "UPDATE", "DELETE" 등

    public static ReviewEventDTOV1 from(Long restaurantId, int rating, String eventType) {
        return ReviewEventDTOV1.builder()
                .restaurantId(restaurantId)
                .rating(rating)
                .eventType(eventType)
                .build();
    }
}
