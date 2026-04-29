package com.careercompass.ai.config;

import com.careercompass.ai.dto.GenericResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

/**
 * Global exception handler.
 * Ensures that:
 *   - No internal stack traces or class names leak to the client
 *   - All auth errors use generic messages
 *   - Every response follows the GenericResponse shape
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<GenericResponse> handleResponseStatus(ResponseStatusException ex) {
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(GenericResponse.builder()
                        .message(ex.getReason() != null ? ex.getReason() : "An error occurred")
                        .build());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<GenericResponse> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(GenericResponse.builder()
                        .message("Invalid credentials")
                        .build());
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<GenericResponse> handleLocked(LockedException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(GenericResponse.builder()
                        .message("Account temporarily locked. Try again later.")
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponse> handleGeneric(Exception ex) {
        log.error("Unhandled exception", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(GenericResponse.builder()
                        .message("An unexpected error occurred")
                        .build());
    }
}
