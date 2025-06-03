package dev.jonas.library.dtos.loan;

import dev.jonas.library.dtos.book.BookDetailsDTO;
import dev.jonas.library.dtos.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * DTO representing a loan record.
 * Includes book and user info, borrow/due/return dates.
 */
@Getter
@Setter
@AllArgsConstructor
public class LoanDTO {
    private Long loanId;
    private BookDetailsDTO book;
    private UserDTO user;
    private LocalDate borrowedDate;
    private LocalDate dueDate;
    private LocalDate returnedDate;
}
