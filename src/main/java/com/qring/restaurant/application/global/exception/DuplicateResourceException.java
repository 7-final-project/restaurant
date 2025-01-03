package com.qring.restaurant.application.global.exception;

import lombok.Getter;

@Getter
public class DuplicateResourceException extends RestaurantException{
    public DuplicateResourceException(String message) {
        super(ErrorCode.DUPLICATE_ERROR, message);
    }
}
