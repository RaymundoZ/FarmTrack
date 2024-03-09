package com.raymundo.farmtrack.controller.advice;

import com.raymundo.farmtrack.dto.basic.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalTime;

@RestControllerAdvice
public class AuthAdvice {

    @ExceptionHandler(value = AuthenticationException.class)
    public ResponseEntity<ErrorDto> handleAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDto(
                HttpStatus.FORBIDDEN.value(),
                e.getClass().getSimpleName(),
                e.getMessage(),
                LocalTime.now()
        ));
    }
}
