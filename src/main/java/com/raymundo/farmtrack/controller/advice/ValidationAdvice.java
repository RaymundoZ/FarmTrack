package com.raymundo.farmtrack.controller.advice;

import com.raymundo.farmtrack.dto.basic.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalTime;
import java.util.Iterator;

@RestControllerAdvice
public class ValidationAdvice {

    @ExceptionHandler(value = BindException.class)
    public ResponseEntity<ErrorDto> handleValidationException(BindException e) {
        return ResponseEntity.badRequest().body(new ErrorDto(
                HttpStatus.BAD_REQUEST.value(),
                e.getClass().getSimpleName(),
                handleValidationResults(e),
                LocalTime.now()
        ));
    }

    private String handleValidationResults(BindException e) {
        StringBuilder message = new StringBuilder();
        Iterator<FieldError> iterator = e.getFieldErrors().iterator();
        while (iterator.hasNext()) {
            FieldError error = iterator.next();
            message.append(error.getField()).append(": ").append(error.getDefaultMessage());
            if (iterator.hasNext())
                message.append(", ");
        }
        return message.toString();
    }
}
