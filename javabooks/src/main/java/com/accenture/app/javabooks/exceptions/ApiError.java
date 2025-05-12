package com.accenture.app.javabooks.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiError {
    private int status;
    private String message;
    private LocalDateTime timestamp;
}
