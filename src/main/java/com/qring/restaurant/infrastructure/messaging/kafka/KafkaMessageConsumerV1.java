package com.qring.restaurant.infrastructure.messaging.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qring.restaurant.domain.model.RestaurantEntity;
import com.qring.restaurant.domain.repository.RestaurantRepository;
import com.qring.restaurant.infrastructure.messaging.dto.ReviewEventDTOV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "KafkaMessageConsumerV1 Log")
public class KafkaMessageConsumerV1 {

    private final RestaurantRepository restaurantRepository;

    // JSON 문자열을 DTO로 변환하기 위한 Jackson ObjectMapper
    private final ObjectMapper objectMapper;


    @KafkaListener(topics = "review-event-topic", groupId = "restaurant-service")
    @Transactional
    public void consumeReviewEvent(String message) {
        try {
            // JSON 메시지를 ReviewEventDTOV1 객체로 변환
            ReviewEventDTOV1 reviewEvent = objectMapper.readValue(message, ReviewEventDTOV1.class);

            log.info("리뷰 이벤트 수신 성공: {}", reviewEvent);

            if ("CREATE".equalsIgnoreCase(reviewEvent.getEventType())) {
                updateRestaurantRating(reviewEvent);
            }

            // 다른 이벤트 타입 처리 가능 (예: UPDATE, DELETE)
        } catch (Exception e) {
            log.error("리뷰 이벤트 처리 실패: {}", message, e);
        }
    }


    private void updateRestaurantRating(ReviewEventDTOV1 reviewEvent) {
        RestaurantEntity restaurant = restaurantRepository.findByIdAndDeletedAtIsNull(reviewEvent.getRestaurantId())
                .orElseThrow(() -> new IllegalArgumentException("해당 레스토랑을 찾을 수 없습니다: " + reviewEvent.getRestaurantId()));

        double newRatingAverage = (double) reviewEvent.getTotalRating() / reviewEvent.getReviewCount();

        restaurant.updateRatingAverage(newRatingAverage);

        restaurant.updateModifiedBy("system");

        log.info("레스토랑 ID {}의 평점이 업데이트되었습니다: {}", restaurant.getId(), newRatingAverage);
    }
}
