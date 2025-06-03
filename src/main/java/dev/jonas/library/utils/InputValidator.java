package dev.jonas.library.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Utility class for input validation.
 * Provides methods to validate input values and throw appropriate exceptions.
 */
public class InputValidator {

    // #################### [ Search Parameters Validation ] ####################

    /**
     * Validates that at least one search parameter (title or author) is provided.
     *
     * @param title  the book title to search for
     * @param author the author name to search for
     * @throws ResponseStatusException if neither title nor author is provided
     */
    public static void requireAtLeastOneSearchParam(String title, String author) {
        if ((title == null || title.isBlank()) && (author == null || author.isBlank())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "At least one search parameter (title or author) is required."
            );
        }
    }

    // #################### [ Null Validation ] ####################

    /**
     * Validates that the provided value is not null.
     *
     * @param value     the value to check
     * @param fieldName the name of the field to use in the error message
     * @throws ResponseStatusException if the value is null
     */
    public static void requireNonNull(Object value, String fieldName) {
        if (value == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    fieldName + " must not be null."
            );
        }
    }

    /**
     * Validates that the provided ID is not null.
     *
     * @param id        the ID to check
     * @param fieldName the name of the field to use in the error message
     * @throws ResponseStatusException if the ID is null
     */
    public static void requireNonNullId(Object id, String fieldName) {
        if (id == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    fieldName + " must not be null."
            );
        }
    }

    // #################### [ Blank String Validation ] ####################

    /**
     * Validates that the provided string is not null or blank.
     *
     * @param value     the string value to check
     * @param fieldName the name of the field to use in the error message
     * @throws ResponseStatusException if the string is null or blank
     */
    public static void requireNonBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    fieldName + " must not be blank."
            );
        }
    }

    // #################### [ Positive Integer Validation ] ####################

    /**
     * Validates that the provided integer value is positive.
     *
     * @param value     the integer value to check
     * @param fieldName the name of the field to use in the error message
     * @throws ResponseStatusException if the value is null or not positive
     */
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