package com.raymundo.farmtrack.controller.advice;

import com.raymundo.farmtrack.dto.basic.ErrorDto;
import com.raymundo.farmtrack.util.exception.GradeException;
import com.raymundo.farmtrack.util.exception.NotFoundException;
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

    /**
     * Exception handler for handling BindException.
     * <p>
     * This method handles BindException by returning a {@link ResponseEntity} with an error message
     * when validation fails due to data binding errors. It returns a status code 400 (BAD REQUEST)
     * along with details of the exception, including its class name, the results of validation errors,
     * and the current time.
     *
     * @param e The BindException instance to be handled.
     * @return A {@link ResponseEntity} containing details of the validation error.
     */
    @ExceptionHandler(value = BindException.class)
    public ResponseEntity<ErrorDto> handleValidationException(BindException e) {
        return ResponseEntity.badRequest().body(new ErrorDto(
                HttpStatus.BAD_REQUEST.value(),
                e.getClass().getSimpleName(),
                handleValidationResults(e),
                LocalTime.now()
        ));
    }

    /**
     * Exception handler for handling NotFoundException.
     * <p>
     * This method handles NotFoundException by returning a {@link ResponseEntity} with an error message
     * when a resource is not found. It returns a status code 400 (BAD REQUEST) along with details
     * of the exception, including its class name, message, and the current time.
     *
     * @param e The NotFoundException instance to be handled.
     * @return A {@link ResponseEntity} containing details of the resource not found error.
     */
    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<ErrorDto> handleNotFoundException(NotFoundException e) {
        return ResponseEntity.badRequest().body(new ErrorDto(
                HttpStatus.BAD_REQUEST.value(),
                e.getClass().getSimpleName(),
                e.getMessage(),
                LocalTime.now()
        ));
    }

    /**
     * Exception handler for handling GradeException.
     * <p>
     * This method handles GradeException by returning a {@link ResponseEntity} with an error message
     * when an error occurs related to grading. It returns a status code 400 (BAD REQUEST) along with details
     * of the exception, including its class name, message, and the current time.
     *
     * @param e The GradeException instance to be handled.
     * @return A {@link ResponseEntity} containing details of the grading error.
     */
    @ExceptionHandler(value = GradeException.class)
    public ResponseEntity<ErrorDto> handleGradeException(GradeException e) {
        return ResponseEntity.badRequest().body(new ErrorDto(
                HttpStatus.BAD_REQUEST.value(),
                e.getClass().getSimpleName(),
                e.getMessage(),
                LocalTime.now()
        ));
    }

    /**
     * Method for handling validation results.
     * <p>
     * This method processes the validation results from a BindException and generates a formatted error message
     * containing field names and corresponding error messages. It iterates through the FieldError objects
     * and appends the field name and error message to a StringBuilder. If there are multiple errors, it separates
     * them with commas. The generated error message is then returned as a String.
     *
     * @param e The BindException containing validation errors.
     * @return A String representing the formatted error message with field names and error messages.
     */
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
