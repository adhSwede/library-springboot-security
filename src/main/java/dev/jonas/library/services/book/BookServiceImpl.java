package dev.jonas.library.services.book;

import dev.jonas.library.dtos.book.BookDetailsDTO;
import dev.jonas.library.dtos.book.BookInputDTO;
import dev.jonas.library.entities.Author;
import dev.jonas.library.entities.Book;
import dev.jonas.library.exceptions.api.BookUnavailableException;
import dev.jonas.library.mappers.DtoToEntityMapper;
import dev.jonas.library.mappers.EntityToDtoMapper;
import dev.jonas.library.repositories.AuthorRepository;
import dev.jonas.library.repositories.BookRepository;
import dev.jonas.library.utils.EntityFetcher;
import dev.jonas.library.utils.InputValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation for managing books in the library.
 * Supports adding books, searching (with or without pagination),
 * and updating available copy counts during loan operations.
 */
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    // ========== [ GET ] ==========
    @Override
    public Page<BookDetailsDTO> getBooksFilteredAndPaged(String title, String author, Pageable pageable) {
        boolean isTitleBlank = (title == null || title.isBlank());
        boolean isAuthorBlank = (author == null || author.isBlank());

        Page<Book> books;

        if (isTitleBlank && isAuthorBlank) {
            books = bookRepository.findAll(pageable);
        } else {
            books = bookRepository.searchBooksPaged(title, author, pageable);
        }

        return books.map(EntityToDtoMapper::mapToBookDetailsDto);
    }

    @Override
    public List<BookDetailsDTO> searchBooksSimple(String title, String author) {
        InputValidator.requireAtLeastOneSearchParam(title, author);

        List<Book> results = bookRepository.searchBooks(title, author);

        return results.stream()
                .map(EntityToDtoMapper::mapToBookDetailsDto)
                .toList();

    }

    // ========== [ POST ] ==========
    @Override
    public BookDetailsDTO addBook(BookInputDTO dto) {
        Author author = EntityFetcher.getAuthorOrThrow(dto.getAuthorId(), authorRepository);

        Book book = DtoToEntityMapper.mapToBookEntity(dto, author);
        Book savedBook = bookRepository.save(book);

        return EntityToDtoMapper.mapToBookDetailsDto(savedBook);
    }

    // ========== [ PUT ] ==========
    public void decrementAvailableCopies(Long bookId) {
        Book book = EntityFetcher.getBookOrThrow(bookId, bookRepository);

        if (book.getAvailableCopies() <= 0) {
            throw new BookUnavailableException("Book with ID " + bookId + " has no copies available for loan.");
        }

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);
    }

    public void incrementAvailableCopies(Long bookId) {
        Book book = EntityFetcher.getBookOrThrow(bookId, bookRepository);

        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);
    }

}