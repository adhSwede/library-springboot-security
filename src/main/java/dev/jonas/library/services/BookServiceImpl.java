package dev.jonas.library.services;

import dev.jonas.library.dtos.book.BookDetailsDTO;
import dev.jonas.library.dtos.book.BookInputDTO;
import dev.jonas.library.entities.Author;
import dev.jonas.library.entities.Book;
import dev.jonas.library.exceptions.BookUnavailableException;
import dev.jonas.library.mappers.DtoToEntityMapper;
import dev.jonas.library.mappers.EntityToDtoMapper;
import dev.jonas.library.repositories.AuthorRepository;
import dev.jonas.library.repositories.BookRepository;
import dev.jonas.library.utils.EntityFetcher;
import dev.jonas.library.utils.InputValidator;
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
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    /**
     * Constructs a BookServiceImpl with the given repositories.
     *
     * @param bookRepository   repository for Book entities
     * @param authorRepository repository for Author entities
     */
    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    // #################### [ GET ] ####################

    /**
     * Returns a paginated and optionally filtered list of books.
     *
     * @param title    optional title filter
     * @param author   optional author name filter
     * @param pageable pagination and sorting information
     * @return a page of BookDetailsDTO objects
     */
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

    /**
     * Searches for books matching the given title and/or author.
     *
     * @param title  optional title keyword
     * @param author optional author name keyword
     * @return a list of matching BookDetailsDTO objects
     */
    @Override
    public List<BookDetailsDTO> searchBooksSimple(String title, String author) {
        InputValidator.requireAtLeastOneSearchParam(title, author);

        List<Book> results = bookRepository.searchBooks(title, author);

        return results.stream()
                .map(EntityToDtoMapper::mapToBookDetailsDto)
                .toList();

    }


    /**
     * Adds a new book to the system.
     *
     * @param dto the input data for the new book
     * @return the saved book as a BookDetailsDTO
     */
    // #################### [ POST ] ####################
    @Override
    public BookDetailsDTO addBook(BookInputDTO dto) {
        InputValidator.requireNonBlank(dto.getTitle(), "Title");
        InputValidator.requireNonNullId(dto.getAuthorId(), "Author ID");
        InputValidator.requirePositive(dto.getTotalCopies(), "Total copies");
        InputValidator.requirePositive(dto.getAvailableCopies(), "Available copies");
        InputValidator.requirePositive(dto.getPublicationYear(), "Publication year");

        Author author = EntityFetcher.getAuthorOrThrow(dto.getAuthorId(), authorRepository);

        Book book = DtoToEntityMapper.mapToBookEntity(dto, author);
        Book savedBook = bookRepository.save(book);

        return EntityToDtoMapper.mapToBookDetailsDto(savedBook);
    }

    /**
     * Decreases the number of available copies for a book.
     * Throws an exception if no copies are available.
     *
     * @param bookId the ID of the book to update
     * @throws BookUnavailableException if no copies are available
     */
    // #################### [ PUT ] ####################
    public void decrementAvailableCopies(Long bookId) {
        Book book = EntityFetcher.getBookOrThrow(bookId, bookRepository);

        if (book.getAvailableCopies() <= 0) {
            throw new BookUnavailableException("Book with ID " + bookId + " has no copies available for loan.");
        }

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);
    }

    /**
     * Increases the number of available copies for a book.
     *
     * @param bookId the ID of the book to update
     */
    public void incrementAvailableCopies(Long bookId) {
        Book book = EntityFetcher.getBookOrThrow(bookId, bookRepository);

        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);
    }

}