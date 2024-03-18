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

    /**
     * Exception handler for handling AuthenticationException.
     * <p>
     * This method handles AuthenticationException by returning a {@link ResponseEntity} with an error message
     * when authentication fails. It returns a status code 403 (FORBIDDEN) along with details of the exception,
     * including its class name, message, and the current time.
     *
     * @param e The AuthenticationException instance to be handled.
     * @return A {@link ResponseEntity} containing details of the authentication error.
     */
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
