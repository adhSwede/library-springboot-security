package dev.jonas.library.repositories;

import dev.jonas.library.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    List<Author> findByLastNameContainingIgnoreCase(String lastName);

}
