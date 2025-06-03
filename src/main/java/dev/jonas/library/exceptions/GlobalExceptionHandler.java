package dev.jonas.library.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * Centralized handler for all exceptions thrown by controllers.
 * <p>
 * Converts exceptions into consistent API responses using {@link ErrorResponse}.
 */
@ControllerAdvice
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles generic Spring {@code ResponseStatusException}.
     */
    @ExceptionHandler(org.springframework.web.server.ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(org.springframework.web.server.ResponseStatusException ex) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        ErrorResponse error = new ErrorResponse(status.value(), status.getReasonPhrase(), ex.getReason());
        return ResponseEntity.status(status).body(error);
    }

    /**
     * Handles missing user errors.
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Handles missing book errors.
     */
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<String> handleBookNotFound(BookNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Handles missing author errors.
     */
    @ExceptionHandler(AuthorNotFoundException.class)
    public ResponseEntity<String> handleAuthorNotFound(AuthorNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Handles missing loan errors.
     */
    @ExceptionHandler(LoanNotFoundException.class)
    public ResponseEntity<String> handleLoanNotFound(LoanNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Handles attempts to return a loan that was already returned.
     */
    @ExceptionHandler(LoanAlreadyReturnedException.class)
    public ResponseEntity<String> handleLoanAlreadyReturned(LoanAlreadyReturnedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * Handles when a book is unavailable for borrowing.
     */
    @ExceptionHandler(BookUnavailableException.class)
    public ResponseEntity<String> handleBookUnavailable(BookUnavailableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * Catches any unhandled exceptions.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleOtherExceptions(Exception ex) {
        ErrorResponse error = new ErrorResponse(500, "Internal Server Error", "Something went wrong.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    /**
     * Handles invalid endpoints (404).
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFound(NoHandlerFoundException ex) {
        ErrorResponse error = new ErrorResponse(404, "Not Found", "This endpoint does not exist.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
