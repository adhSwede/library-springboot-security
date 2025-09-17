package dev.jonas.library.mappers;

import dev.jonas.library.dtos.author.AuthorDTO;
import dev.jonas.library.dtos.book.BookDetailsDTO;
import dev.jonas.library.dtos.loan.LoanDTO;
import dev.jonas.library.dtos.user.UserDTO;
import dev.jonas.library.entities.Author;
import dev.jonas.library.entities.Book;
import dev.jonas.library.entities.Loan;
import dev.jonas.library.entities.User;

/**
 * Utility class for converting entities into Data Transfer Objects (DTOs).
 * Used to format output data sent back to clients.
 */
public class EntityToDtoMapper {

    // ==================== [ Authors ] ====================
    public static AuthorDTO mapToAuthorDto(Author author) {
        return new AuthorDTO(
                author.getAuthorId(),
                author.getFirstName(),
                author.getLastName(),
                author.getBirthYear(),
                author.getNationality()
        );
    }

    // ==================== [ Books ] ====================
    public static BookDetailsDTO mapToBookDetailsDto(Book book) {
        return new BookDetailsDTO(
                book.getBookId(),
                book.getTitle(),
                book.getAuthor().getFirstName() + " " + book.getAuthor().getLastName(),
                book.getPublicationYear(),
                book.getTotalCopies(),
                book.getAvailableCopies()
        );
    }

    // ==================== [ Books – Simple ] ====================
    public static BookDetailsDTO mapToBookDetailsDtoSimple(Book book) {
        return new BookDetailsDTO(
                book.getBookId(),
                book.getTitle(),
                null,
                null,
                null,
                null
        );
    }

    // ==================== [ Loans ] ====================
    public static LoanDTO mapToLoanDto(Loan loan) {
        return new LoanDTO(
                loan.getLoanId(),
                EntityToDtoMapper.mapToBookDetailsDto(loan.getBook()),
                EntityToDtoMapper.mapToUserDto(loan.getUser()),
                loan.getBorrowedDate(),
                loan.getDueDate(),
                loan.getReturnedDate()
        );
    }

    // ==================== [ Users ] ====================
    public static UserDTO mapToUserDto(User user) {
        return new UserDTO(
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRegistrationDate()
        );
    }

    // ==================== [ Users – Simple ] ====================
    public static UserDTO mapToUserDtoSimple(User user) {
        return new UserDTO(
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                null
        );
    }
}