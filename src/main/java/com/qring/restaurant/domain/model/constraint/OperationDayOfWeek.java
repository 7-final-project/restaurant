package com.qring.restaurant.domain.model.constraint;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OperationDayOfWeek {
    MONDAY("월요일"),
    TUESDAY("화요일"),
    WEDNESDAY("수요일"),
    THURSDAY("목요일"),
    FRIDAY("금요일"),
    SATURDAY("토요일"),
    SUNDAY("일요일");

    private final String description;

    public String getDescription() {
        return description;
    }

    public static OperationDayOfWeek fromString(String description) {
        return switch (description) {
            case "월요일" -> OperationDayOfWeek.MONDAY;
            case "화요일" -> OperationDayOfWeek.TUESDAY;
            case "수요일" -> OperationDayOfWeek.WEDNESDAY;
            case "목요일" -> OperationDayOfWeek.THURSDAY;
            case "금요일" -> OperationDayOfWeek.FRIDAY;
            case "토요일" -> OperationDayOfWeek.SATURDAY;
            case "일요일" -> OperationDayOfWeek.SUNDAY;
            default -> throw new IllegalArgumentException("유효하지 않은 요일입니다: " + description);
        };
    }
}
