package dev.jonas.library.dtos.book;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object for incoming Book data.
 * Used when creating or updating books.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class BookInputDTO {
    private String title;
    @NotNull(message = "Publication year must not be null.")
    private Integer publicationYear;
    private Integer availableCopies;
    private Integer totalCopies;
    private Long authorId;
}
