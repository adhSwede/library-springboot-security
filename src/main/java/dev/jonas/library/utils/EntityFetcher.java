package dev.jonas.library.utils;

import dev.jonas.library.entities.*;
import dev.jonas.library.exceptions.api.AuthorNotFoundException;
import dev.jonas.library.exceptions.api.BookNotFoundException;
import dev.jonas.library.exceptions.api.LoanNotFoundException;
import dev.jonas.library.exceptions.api.UserNotFoundException;
import dev.jonas.library.exceptions.security.RoleNotFoundException;
import dev.jonas.library.repositories.*;

/**
 * Utility class to fetch entities from the database.
 * Provides methods to retrieve entities by their ID or throw an exception if not found.
 */
public class EntityFetcher {

    // #################### [ Author Fetching ] ####################
    public static Author getAuthorOrThrow(Long authorId, AuthorRepository repo) {
        return repo.findById(authorId)
                .orElseThrow(() -> new AuthorNotFoundException("Author with ID " + authorId + " not found"));
    }

    // #################### [ Book Fetching ] ####################
    public static Book getBookOrThrow(Long bookId, BookRepository repo) {
        return repo.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book with ID " + bookId + " not found"));
    }

    // #################### [ User Fetching ] ####################
    public static User getUserOrThrow(Long userId, UserRepository repo) {
        return repo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));
    }

    public static User getUserOrThrow(String email, UserRepository repo) {
        if (email == null || email.equals("anonymousUser")) {
            throw new UserNotFoundException("No such user found");
        }
        return repo.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
    }

    // #################### [ Loan Fetching ] ####################
    public static Loan getLoanOrThrow(Long loanId, LoanRepository repo) {
        return repo.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException("Loan with ID " + loanId + " not found"));
    }

    // #################### [ Role Fetching ] ####################
    public static Role getRoleOrThrow(String roleName, RoleRepository repo) {
        return repo.findByRoleName(roleName)
                .orElseThrow(() -> new RoleNotFoundException("Role with name " + roleName + " not found"));
    }
}