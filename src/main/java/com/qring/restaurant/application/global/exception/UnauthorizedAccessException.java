package com.qring.restaurant.application.global.exception;

import lombok.Getter;

@Getter
public class UnauthorizedAccessException extends RestaurantException{
    public UnauthorizedAccessException(String message) {
        super(ErrorCode.AUTHORITY_ERROR, message);
    }
}
