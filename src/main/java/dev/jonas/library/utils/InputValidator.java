package dev.jonas.library.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Utility class for input validation.
 * Provides methods to validate input values and throw appropriate exceptions.
 * {@link Deprecated} methods Replaced by Spring's built-in validation mechanisms.
 */
public class InputValidator {

    // ========== [ Search Parameters Validation ] ==========
    public static void requireAtLeastOneSearchParam(String title, String author) {
        if ((title == null || title.isBlank()) && (author == null || author.isBlank())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "At least one search parameter (title or author) is required."
            );
        }
    }

    @Deprecated
    // ========== [ Null Validation ] ==========
    public static void requireNonNull(Object value, String fieldName) {
        if (value == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    fieldName + " must not be null."
            );
        }
    }

    @Deprecated
    public static void requireNonNullId(Object id, String fieldName) {
        if (id == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    fieldName + " must not be null."
            );
        }
    }

    @Deprecated
    // ========== [ Blank String Validation ] ==========
    public static void requireNonBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    fieldName + " must not be blank."
            );
        }
    }

    @Deprecated
    // ========== [ Positive Integer Validation ] ==========
    public static void requirePositive(Integer value, String fieldName) {
        if (value == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    fieldName + " must not be null."
            );
        }
        if (value <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    fieldName + " must be greater than 0."
            );
        }
    }
}