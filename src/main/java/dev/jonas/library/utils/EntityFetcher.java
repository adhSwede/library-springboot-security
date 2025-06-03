package dev.jonas.library.utils;

import dev.jonas.library.entities.Author;
import dev.jonas.library.entities.Book;
import dev.jonas.library.entities.Loan;
import dev.jonas.library.entities.User;
import dev.jonas.library.exceptions.AuthorNotFoundException;
import dev.jonas.library.exceptions.BookNotFoundException;
import dev.jonas.library.exceptions.LoanNotFoundException;
import dev.jonas.library.exceptions.UserNotFoundException;
import dev.jonas.library.repositories.AuthorRepository;
import dev.jonas.library.repositories.BookRepository;
import dev.jonas.library.repositories.LoanRepository;
import dev.jonas.library.repositories.UserRepository;

/**
 * Utility class to fetch entities from the database.
 * Provides methods to retrieve entities by their ID or throw an exception if not found.
 */
public class EntityFetcher {

    // #################### [ Author Fetching ] ####################

    /**
     * Fetches an Author entity by its ID, or throws an exception if not found.
     *
     * @param authorId the ID of the Author to fetch
     * @param repo     the repository to perform the search
     * @return the Author entity
     * @throws AuthorNotFoundException if the Author with the given ID is not found
     */
    public static Author getAuthorOrThrow(Long authorId, AuthorRepository repo) {
        return repo.findById(authorId)
                .orElseThrow(() -> new AuthorNotFoundException("Author with ID " + authorId + " not found"));
    }

    // #################### [ Book Fetching ] ####################

    /**
     * Fetches a Book entity by its ID, or throws an exception if not found.
     *
     * @param bookId the ID of the Book to fetch
     * @param repo   the repository to perform the search
     * @return the Book entity
     * @throws BookNotFoundException if the Book with the given ID is not found
     */
    public static Book getBookOrThrow(Long bookId, BookRepository repo) {
        return repo.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book with ID " + bookId + " not found"));
    }

    // #################### [ User Fetching ] ####################

    /**
     * Fetches a User entity by its ID, or throws an exception if not found.
     *
     * @param userId the ID of the User to fetch
     * @param repo   the repository to perform the search
     * @return the User entity
     * @throws UserNotFoundException if the User with the given ID is not found
     */
    public static User getUserOrThrow(Long userId, UserRepository repo) {
        return repo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));
    }

    // #################### [ Loan Fetching ] ####################

    /**
     * Fetches a Loan entity by its ID, or throws an exception if not found.
     *
     * @param loanId the ID of the Loan to fetch
     * @param repo   the repository to perform the search
     * @return the Loan entity
     * @throws LoanNotFoundException if the Loan with the given ID is not found
     */
    public static Loan getLoanOrThrow(Long loanId, LoanRepository repo) {
        return repo.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException("Loan with ID " + loanId + " not found"));
    }
}