package dev.jonas.library.services;

import dev.jonas.library.dtos.book.BookDetailsDTO;
import dev.jonas.library.dtos.book.BookInputDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookService {

    // #################### [ GET ] ####################
    Page<BookDetailsDTO> getBooksFilteredAndPaged(String title, String author, Pageable pageable);

    List<BookDetailsDTO> searchBooksSimple(String title, String author);

    // #################### [ POST ] ####################
    BookDetailsDTO addBook(BookInputDTO dto);

}
