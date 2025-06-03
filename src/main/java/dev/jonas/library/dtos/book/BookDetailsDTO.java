package dev.jonas.library.dtos.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object for returning Book data.
 * Used primarily in responses.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDetailsDTO {
    private Long bookId;
    private String title;
    private String authorName;
    private Integer publicationYear;
    private Integer totalCopies;
    private Integer availableCopies;
}