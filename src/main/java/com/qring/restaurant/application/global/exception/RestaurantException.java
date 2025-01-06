package com.qring.restaurant.application.global.exception;

import lombok.Getter;

@Getter
public class RestaurantException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String message;

    public RestaurantException(ErrorCode errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }


}
