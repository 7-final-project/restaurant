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
            // JSON 메시지를 DTO로 변환
            ReviewEventDTOV1 reviewEvent = objectMapper.readValue(message, ReviewEventDTOV1.class);

            log.info("리뷰 이벤트 수신 성공: {}", reviewEvent);

            // 이벤트 타입에 따라 처리
            switch (reviewEvent.getEventType().toUpperCase()) {
                case "CREATE":
                    handleCreateEvent(reviewEvent);
                    break;
                case "UPDATE":
                    handleUpdateEvent(reviewEvent);
                    break;
                case "DELETE":
                    handleDeleteEvent(reviewEvent);
                    break;
                default:
                    log.warn("알 수 없는 이벤트 타입: {}", reviewEvent.getEventType());
            }
        } catch (Exception e) {
            log.error("리뷰 이벤트 처리 실패: {}", message, e);
        }
    }

    private void handleCreateEvent(ReviewEventDTOV1 reviewEvent) {
        RestaurantEntity restaurant = restaurantRepository.findByIdAndDeletedAtIsNull(reviewEvent.getRestaurantId())
                .orElseThrow(() -> new IllegalArgumentException("해당 레스토랑을 찾을 수 없습니다: " + reviewEvent.getRestaurantId()));

        // 리뷰 통계 업데이트
        restaurant.incrementReviewCount();
        restaurant.addTotalRating(reviewEvent.getRating());

        log.info("CREATE 이벤트로 레스토랑 ID {}의 통계가 업데이트되었습니다: 총 리뷰 수 = {}, 총 평점 = {}",
                restaurant.getId(), restaurant.getReviewCount(), restaurant.getTotalRating());
    }

    private void handleUpdateEvent(ReviewEventDTOV1 reviewEvent) {
        RestaurantEntity restaurant = restaurantRepository.findByIdAndDeletedAtIsNull(reviewEvent.getRestaurantId())
                .orElseThrow(() -> new IllegalArgumentException("해당 레스토랑을 찾을 수 없습니다: " + reviewEvent.getRestaurantId()));

        // 리뷰 통계 업데이트
        restaurant.updateTotalRating(reviewEvent.getRating());

        log.info("UPDATE 이벤트로 레스토랑 ID {}의 통계가 업데이트되었습니다: 총 평점 = {}",
                restaurant.getId(), restaurant.getTotalRating());
    }

    private void handleDeleteEvent(ReviewEventDTOV1 reviewEvent) {
        RestaurantEntity restaurant = restaurantRepository.findByIdAndDeletedAtIsNull(reviewEvent.getRestaurantId())
                .orElseThrow(() -> new IllegalArgumentException("해당 레스토랑을 찾을 수 없습니다: " + reviewEvent.getRestaurantId()));

        // 리뷰 통계 업데이트
        restaurant.decrementReviewCount();
        restaurant.subtractTotalRating(reviewEvent.getRating());

        log.info("DELETE 이벤트로 레스토랑 ID {}의 통계가 업데이트되었습니다: 총 리뷰 수 = {}, 총 평점 = {}",
                restaurant.getId(), restaurant.getReviewCount(), restaurant.getTotalRating());
    }
}
