package com.qring.restaurant.application.global.exception;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RestaurantException{
    public EntityNotFoundException(String message) {
        super(ErrorCode.NOT_FOUND_ERROR, message);
    }
}
