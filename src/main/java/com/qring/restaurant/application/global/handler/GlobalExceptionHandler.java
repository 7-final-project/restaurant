package com.qring.restaurant.application.global.handler;

import com.qring.restaurant.application.global.dto.ResDTO;
import com.qring.restaurant.application.global.exception.RestaurantException;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({RestaurantException.class})
    public ResponseEntity<ResDTO<Object>> RestaurantExceptionHandler(RestaurantException ex) {
        return new ResponseEntity<>(
                ResDTO.builder()
                        .code(ex.getErrorCode().getCode())
                        .message(ex.getMessage())
                        .build(),
                ex.getErrorCode().getHttpStatus()
        );
    }
}
