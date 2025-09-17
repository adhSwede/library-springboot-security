package dev.jonas.library.mappers;

import dev.jonas.library.dtos.author.AuthorInputDTO;
import dev.jonas.library.dtos.book.BookInputDTO;
import dev.jonas.library.dtos.loan.LoanCreateDTO;
import dev.jonas.library.dtos.user.UserInputDTO;
import dev.jonas.library.entities.Author;
import dev.jonas.library.entities.Book;
import dev.jonas.library.entities.Loan;
import dev.jonas.library.entities.User;

import java.time.LocalDate;

/**
 * Utility class for converting Data Transfer Objects (DTOs) into entity objects.
 * Used during the creation of new domain instances based on incoming client data.
 */
public class DtoToEntityMapper {

    // ==================== [ Authors ] ====================
    public static Author mapToAuthorEntity(AuthorInputDTO dto) {
        Author author = new Author();
        author.setFirstName(dto.getFirstName());
        author.setLastName(dto.getLastName());
        author.setBirthYear(dto.getBirthYear());
        author.setNationality(dto.getNationality());
        return author;
    }

    // ==================== [ Books ] ====================
    public static Book mapToBookEntity(BookInputDTO dto, Author author) {
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setPublicationYear(dto.getPublicationYear());
        book.setAvailableCopies(dto.getAvailableCopies());
        book.setTotalCopies(dto.getTotalCopies());
        book.setAuthor(author);
        return book;
    }

    // ==================== [ Loans ] ====================
    public static Loan mapToLoanEntity(LoanCreateDTO dto, User user, Book book) {
        Loan loan = new Loan();
        loan.setUser(user);
        loan.setBook(book);
        loan.setBorrowedDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(14));
        loan.setReturnedDate(null);
        return loan;
    }

    // ==================== [ Users ] ====================
    public static User mapToUserEntity(UserInputDTO dto) {
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        return user;
    }
}