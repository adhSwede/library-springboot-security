package dev.jonas.library.controllers.open;

import dev.jonas.library.dtos.book.BookDetailsDTO;
import dev.jonas.library.dtos.book.BookInputDTO;
import dev.jonas.library.services.book.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * REST controller for managing books in the library system.
 * Provides endpoints for searching, paginating, and adding books.
 */
@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // ########## [ GET ] ##########

    /**
     * Retrieves a paginated list of books, optionally filtered by title and/or author.
     *
     * @param title    optional title keyword for filtering
     * @param author   optional author name for filtering
     * @param pageable pagination and sorting information
     * @return a page of matching books
     */
    @GetMapping
    public ResponseEntity<Page<BookDetailsDTO>> getBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            Pageable pageable
    ) {
        Page<BookDetailsDTO> books = bookService.getBooksFilteredAndPaged(title, author, pageable);
        return ResponseEntity.ok(books);
    }

    /**
     * Performs a simple keyword search for books by title and/or author.
     *
     * @param title  optional title keyword
     * @param author optional author keyword
     * @return list of matching books
     */
    @GetMapping("/search")
    public ResponseEntity<List<BookDetailsDTO>> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author
    ) {
        List<BookDetailsDTO> results = bookService.searchBooksSimple(title, author);
        return ResponseEntity.ok(results);
    }

    // ########## [ POST ] ##########

    /**
     * Adds a new book to the system.
     *
     * @param dto input data for the book
     * @return the saved book with a Location header
     */
    @PostMapping
    public ResponseEntity<BookDetailsDTO> addBook(@RequestBody BookInputDTO dto) {
        BookDetailsDTO savedBook = bookService.addBook(dto);

        return ResponseEntity
                .created(URI.create("/books/" + savedBook.getBookId()))
                .body(savedBook);
    }

}