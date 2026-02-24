package com.example.coreservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.error("Validation failed: {}", errorMessage);
        return buildResponse(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleDatabaseExceptions(DataAccessException ex) {
        log.error("Database error occurred", ex);
        return buildResponse("A database access error occurred while processing your request",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CustomBusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessExceptions(CustomBusinessException ex) {
        log.error("Business rule violation: {}", ex.getMessage());
        return buildResponse(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralExceptions(Exception ex) {
        log.error("Unhandled exception occurred", ex);
        return buildResponse("An unexpected error occurred in the system", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> buildResponse(String message, HttpStatus status) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .message(message)
                        .status(status.value())
                        .timestamp(LocalDateTime.now())
                        .build(),
                status);
    }
}
