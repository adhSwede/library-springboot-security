package dev.jonas.library.dtos.loan;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for creating a new loan.
 * Contains the IDs of the user and book involved.
 */
@Getter
@Setter
@AllArgsConstructor
public class LoanCreateDTO {
    private Long userId;
    private Long bookId;
}