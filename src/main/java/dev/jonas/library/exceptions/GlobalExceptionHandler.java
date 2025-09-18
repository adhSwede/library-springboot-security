package dev.jonas.library.exceptions;

import dev.jonas.library.exceptions.auth.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final ErrorResponseBuilder errorResponseBuilder;

    // ==================== 404 / Not Found ====================
    // Thrown when a specific resource cannot be located.
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex, HttpServletRequest request) {
        return errorResponseBuilder.buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBookNotFound(BookNotFoundException ex, HttpServletRequest request) {
        return errorResponseBuilder.buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(AuthorNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAuthorNotFound(AuthorNotFoundException ex, HttpServletRequest request) {
        return errorResponseBuilder.buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(LoanNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleLoanNotFound(LoanNotFoundException ex, HttpServletRequest request) {
        return errorResponseBuilder.buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFound(NoHandlerFoundException ex, HttpServletRequest request) {
        return errorResponseBuilder.buildErrorResponse(HttpStatus.NOT_FOUND, "This endpoint does not exist.", request);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRoleNotFound(RoleNotFoundException ex, HttpServletRequest request) {
        return errorResponseBuilder.buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    // ==================== 400 / Bad Request ====================
    // Thrown when the request is syntactically correct but semantically invalid.
    @ExceptionHandler(LoanAlreadyReturnedException.class)
    public ResponseEntity<ErrorResponse> handleLoanAlreadyReturned(LoanAlreadyReturnedException ex, HttpServletRequest request) {
        return errorResponseBuilder.buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return errorResponseBuilder.buildErrorResponse(HttpStatus.BAD_REQUEST, message, request);
    }

    @ExceptionHandler(BookUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleBookUnavailable(BookUnavailableException ex, HttpServletRequest request) {
        return errorResponseBuilder.buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    // ==================== 401 / Unauthorized ====================
    // Thrown when authentication fails (e.g., invalid credentials).
    // NOTE: This does not mean "not allowed" – that's 403.
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex, HttpServletRequest request) {
        return errorResponseBuilder.buildErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ErrorResponse> handleTokenExpired(TokenExpiredException ex, HttpServletRequest request) {
        return errorResponseBuilder.buildErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
    }

    // ==================== 403 / Forbidden ====================
    // Thrown when the user is authenticated but lacks necessary permissions.
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        return errorResponseBuilder.buildErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage(), request);
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleSpringSecurityAccessDenied(org.springframework.security.access.AccessDeniedException ex, HttpServletRequest request) {
        return errorResponseBuilder.buildErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage(), request);
    }

    // ==================== 409 / Conflict ====================
    // Thrown when the request conflicts with the current server state (e.g., duplicate email).
    @ExceptionHandler(EmailAlreadyUsedException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyUsed(EmailAlreadyUsedException ex, HttpServletRequest request) {
        return errorResponseBuilder.buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    // ==================== 423 / Locked ====================
    // Thrown when the requested resource (e.g., user account) is currently locked.
    @ExceptionHandler(AccountLockedException.class)
    public ResponseEntity<ErrorResponse> handleAccountLocked(AccountLockedException ex, HttpServletRequest request) {
        return errorResponseBuilder.buildErrorResponse(HttpStatus.LOCKED, ex.getMessage(), request);
    }

    // ==================== Spring’s ResponseStatusException ====================
    // Catches exceptions thrown by Spring with a predefined status (e.g., via @ResponseStatus).
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        return errorResponseBuilder.buildErrorResponse(status, ex.getReason(), request);
    }

    // ==================== Fallback ====================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleOtherExceptions(Exception ex, HttpServletRequest request) {
        log.error("Unexpected exception", ex);
        return errorResponseBuilder.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
    }
}