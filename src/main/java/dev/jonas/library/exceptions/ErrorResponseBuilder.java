package dev.jonas.library.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;

@Component
public class ErrorResponseBuilder {

    private final Clock clock;

    public ErrorResponseBuilder(Clock clock) {
        this.clock = clock;
    }

    /**
     * Builds a standardized error response using {@link ErrorResponse}.
     */
    public ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String message, HttpServletRequest request) {
        ErrorResponse error = ErrorResponse.builder()
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now(clock))
                .build();
        return ResponseEntity.status(status).body(error);
    }
}