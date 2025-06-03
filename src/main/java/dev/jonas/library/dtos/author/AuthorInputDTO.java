package dev.jonas.library.dtos.author;

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
    private Long authorId;
    private String firstName;
    private String lastName;
    private Integer birthYear;
    private String nationality;
}