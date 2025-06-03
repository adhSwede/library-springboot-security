package dev.jonas.library.dtos.author;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for returning full author data.
 * Used primarily in responses.
 */
@Getter
@Setter
@AllArgsConstructor
public class AuthorDTO {
    private Long authorId;
    private String firstName;
    private String lastName;
    private Integer birthYear;
    private String nationality;
}