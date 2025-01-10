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
    private int reviewCount;
    private int totalRating;
    private String eventType; // "CREATE", "UPDATE", "DELETE" 등

    // from 메서드 추가 (유틸리티로 DTO 생성)
    public static ReviewEventDTOV1 from(Long restaurantId, int rating, int reviewCount, int totalRating, String eventType) {
        return ReviewEventDTOV1.builder()
                .restaurantId(restaurantId)
                .rating(rating)
                .reviewCount(reviewCount)
                .totalRating(totalRating)
                .eventType(eventType)
                .build();
    }
}
