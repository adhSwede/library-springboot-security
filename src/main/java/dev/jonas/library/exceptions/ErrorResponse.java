package dev.jonas.library.exceptions;

/**
 * Standardized structure for sending error information in API responses.
 * <p>
 * Used by the {@link GlobalExceptionHandler} to wrap exception data
 * into a consistent format for the frontend or API consumer.
 */
public class ErrorResponse {

    /**
     * The HTTP status code (e.g. 400, 404, 500).
     */
    private final int status;

    /**
     * The standard HTTP error phrase (e.g. "Bad Request", "Not Found").
     */
    private final String error;

    /**
     * A human-readable explanation of the error.
     */
    private final String message;

    /**
     * Constructs a new {@code ErrorResponse} with the given status, error, and message.
     *
     * @param status  the HTTP status code
     * @param error   the standard HTTP error phrase
     * @param message the detailed error message
     */
    public ErrorResponse(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}