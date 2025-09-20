package dev.jonas.library.services.book;

import dev.jonas.library.dtos.book.BookDetailsDTO;
import dev.jonas.library.dtos.book.BookInputDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookService {
    Page<BookDetailsDTO> getBooksFilteredAndPaged(String title, String author, Pageable pageable);

    List<BookDetailsDTO> searchBooksSimple(String title, String author);

    BookDetailsDTO addBook(BookInputDTO dto);
}
