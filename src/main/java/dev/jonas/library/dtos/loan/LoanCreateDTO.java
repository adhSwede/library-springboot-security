package dev.jonas.library.dtos.loan;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * DTO for creating a new loan.
 * Contains the IDs of the user and book involved.
 */
@Getter
@Setter
@RequiredArgsConstructor
public class LoanCreateDTO {
    @NotNull
    private Long userId;
    @NotNull
    private Long bookId;
}