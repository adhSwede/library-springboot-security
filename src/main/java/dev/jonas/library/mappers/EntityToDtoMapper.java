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

    /**
     * Maps an Author entity to its DTO representation.
     *
     * @param author the Author entity
     * @return the corresponding AuthorDTO
     */
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

    /**
     * Maps a Book entity to its full DTO representation.
     *
     * @param book the Book entity
     * @return the corresponding BookDetailsDTO
     */
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

    /**
     * Maps a Book entity to a simplified DTO with minimal fields.
     * Author name and other details are omitted.
     *
     * @param book the Book entity
     * @return a simplified BookDetailsDTO
     */
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

    /**
     * Maps a Loan entity to its DTO representation.
     * Includes both book and user data as nested DTOs.
     *
     * @param loan the Loan entity
     * @return the corresponding LoanDTO
     */
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

    /**
     * Maps a User entity to its full DTO representation.
     *
     * @param user the User entity
     * @return the corresponding UserDTO
     */
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

    /**
     * Maps a User entity to a simplified DTO with fewer fields.
     * Registration date is omitted.
     *
     * @param user the User entity
     * @return a simplified UserDTO
     */
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