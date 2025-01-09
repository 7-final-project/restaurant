package com.qring.restaurant.infrastructure.messaging.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewEventDTOV1 {
    private Long restaurantId;
    private int rating;
    private int reviewCount;
    private int totalRating;
    private String eventType; // "CREATE", "UPDATE", "DELETE" 등
}
