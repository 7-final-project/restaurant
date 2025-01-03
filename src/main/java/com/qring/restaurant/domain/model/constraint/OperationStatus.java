package com.qring.restaurant.domain.model.constraint;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OperationStatus {
    OPEN(Description.OPEN),
    CLOSED(Description.CLOSED);

    private final String description;

    public static class Description {
        public static final String OPEN = "영업중";
        public static final String CLOSED = "영업 종료";
    }

    public static OperationStatus fromString(String description) {
        return switch (description) {
            case Description.OPEN -> OperationStatus.OPEN;
            case Description.CLOSED -> OperationStatus.CLOSED;
            default -> throw new IllegalArgumentException("유효하지 않은 운영 상태입니다: " + description);
        };
    }
}
