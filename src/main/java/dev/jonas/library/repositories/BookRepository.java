package dev.jonas.library.repositories;

import dev.jonas.library.entities.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    // ########## [ JPQL Search ] ##########
    @Query("SELECT b FROM Book b WHERE " +
            "(:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
            "(:author IS NULL OR (b.author IS NOT NULL AND LOWER(CONCAT(b.author.firstName, ' ', b.author.lastName)) LIKE LOWER(CONCAT('%', :author, '%'))))")
    List<Book> searchBooks(@Param("title") String title, @Param("author") String author);

    // ########## [ Native Query ] ##########
    @Query(value = "SELECT * FROM books WHERE book_id = :id", nativeQuery = true)
    Optional<Book> findBookByIdNative(@Param("id") Long id);

    // ########## [ JPQL Paged Search ] ##########
    @Query("""
            SELECT b FROM Book b
            WHERE (:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%')))
            AND (:author IS NULL OR LOWER(CONCAT(b.author.firstName, ' ', b.author.lastName)) LIKE LOWER(CONCAT('%', :author, '%')))
            """)
    Page<Book> searchBooksPaged(@Param("title") String title, @Param("author") String author, Pageable pageable);
}