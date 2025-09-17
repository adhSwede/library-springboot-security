package dev.jonas.library.dtos.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object for incoming Book data.
 * Used when creating or updating books.
 */
@Getter
@Setter
@RequiredArgsConstructor
public class BookInputDTO {
    @NotBlank
    private String title;

    @NotNull(message = "Publication year must not be null.")
    private Integer publicationYear;

    @NotNull
    private Integer availableCopies;

    @NotNull
    private Integer totalCopies;

    @NotNull
    private Long authorId;
}
