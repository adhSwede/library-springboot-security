package dev.jonas.library.datajpa.repositories;

import dev.jonas.library.entities.Author;
import dev.jonas.library.entities.Book;
import dev.jonas.library.repositories.AuthorRepository;
import dev.jonas.library.repositories.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    @DisplayName("searchBooks() returns book matching title and author")
    void searchBooks_shouldReturnMatchingBook() {
        // ########## [ Arrange ] ##########
        Author author = new Author(
                "Mary",
                "Shelley",
                1797,
                "England"
        );
        author = authorRepository.save(author);

        Book book = new Book("Frankenstein",
                author,
                1818,
                3,
                3);
        bookRepository.save(book);

        // ########## [ Act ] ##########
        List<Book> result = bookRepository.searchBooks(
                "frank",
                "mary shelley"
        );

        // ########## [ Assert ] ##########
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getTitle()).containsIgnoringCase("frank");
    }
}