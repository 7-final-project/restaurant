package com.qring.restaurant.application.global.exception;

import lombok.Getter;

@Getter
public class BadRequestException extends RestaurantException{
    public BadRequestException(String message) {
        super(ErrorCode.BAD_REQUEST_ERROR, message);
    }
}
