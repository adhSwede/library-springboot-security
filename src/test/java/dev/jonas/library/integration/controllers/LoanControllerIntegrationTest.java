package dev.jonas.library.integration.controllers;

import dev.jonas.library.entities.Author;
import dev.jonas.library.entities.Book;
import dev.jonas.library.entities.User;
import dev.jonas.library.repositories.AuthorRepository;
import dev.jonas.library.repositories.BookRepository;
import dev.jonas.library.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class LoanControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @WithMockUser(username = "user@example.com", roles = {"USER", "ADMIN"})
    @Test
    void createLoan_returnsLoanDTO_andDecreasesAvailableCopies() throws Exception {
        // ########## [ Arrange ] ##########
        Author author = new Author(
                "J.R.R.",
                "Tolkien",
                1892,
                "England"
        );
        Author savedAuthor = authorRepository.save(author);

        Book book = new Book(
                "The Hobbit",
                savedAuthor,
                1937,
                5,
                5
        );
        Book savedBook = bookRepository.save(book);

        User user = new User(
                "Hasse",
                "Målvakt",
                "hasse.malvakt@mail.se",
                "123",
                LocalDateTime.now()
        );
        User savedUser = userRepository.save(user);

        String jsonPayload = String.format("""
                {
                  "userId": %d,
                  "bookId": %d
                }
                """, savedUser.getUserId(), savedBook.getBookId());

        // ########## [ Act & Assert ] ##########
        int before = savedBook.getAvailableCopies();

        mockMvc.perform(post("/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.book.title").value("The Hobbit"))
                .andExpect(jsonPath("$.user.firstName").value("Hasse"))
                .andExpect(jsonPath("$.dueDate").exists());

        int after = bookRepository.findById(savedBook.getBookId()).orElseThrow().getAvailableCopies();
        assertEquals(before - 1, after);

    }

}