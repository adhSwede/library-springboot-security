package dev.jonas.library.controllers.api;

import dev.jonas.library.dtos.book.BookDetailsDTO;
import dev.jonas.library.dtos.book.BookInputDTO;
import dev.jonas.library.services.book.BookService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    // ==================== [ GET ] ====================
    @GetMapping
    public ResponseEntity<Page<BookDetailsDTO>> getBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            Pageable pageable
    ) {
        Page<BookDetailsDTO> books = bookService.getBooksFilteredAndPaged(title, author, pageable);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/search")
    public ResponseEntity<List<BookDetailsDTO>> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author
    ) {
        List<BookDetailsDTO> results = bookService.searchBooksSimple(title, author);
        return ResponseEntity.ok(results);
    }

    // ==================== [ POST ] ====================

    /**
     * Admin-only endpoint for adding new books to the library.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDetailsDTO> addBook(@RequestBody @Valid BookInputDTO dto) {
        BookDetailsDTO savedBook = bookService.addBook(dto);

        return ResponseEntity
                .created(URI.create("/books/" + savedBook.getBookId()))
                .body(savedBook);
    }
}