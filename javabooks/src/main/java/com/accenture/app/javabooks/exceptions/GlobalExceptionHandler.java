package com.accenture.app.javabooks.exceptions;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(EntityNotFoundException ex) {
        ApiError error = ApiError.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ApiError> handleEntityExistsException(EntityExistsException ex) {
        ApiError error = ApiError.builder()
                .status(HttpStatus.CONFLICT.value())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        StringBuilder message = new StringBuilder("Validation failed: ");
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            message.append(String.format("[%s: %s] ", fieldError.getField(), fieldError.getDefaultMessage()));
        }

        ApiError error = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(message.toString().trim())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAll(Exception ex) {
        ApiError error = ApiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Unexpected error: " + ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}