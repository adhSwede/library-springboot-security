package dev.jonas.library.dtos.author;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for incoming author data.
 * Used when creating or updating authors.
 */
@Getter
@Setter
@AllArgsConstructor
public class AuthorInputDTO {
    @NotNull
    private Long authorId;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotNull
    private Integer birthYear;
    @NotBlank
    private String nationality;
}