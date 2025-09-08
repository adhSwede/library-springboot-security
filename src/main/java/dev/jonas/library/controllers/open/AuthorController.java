package dev.jonas.library.controllers.open;

import dev.jonas.library.dtos.author.AuthorDTO;
import dev.jonas.library.dtos.author.AuthorInputDTO;
import dev.jonas.library.services.author.AuthorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


/**
 * REST controller for managing authors in the library system.
 * Provides endpoints to retrieve and create authors.
 */
@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    // ==================== [ GET ] ====================

    /**
     * Retrieves all authors in the system.
     *
     * @return a list of AuthorDTOs
     */
    @GetMapping
    public ResponseEntity<List<AuthorDTO>> getAllAuthors() {
        List<AuthorDTO> authors = authorService.getAllAuthorDTOs();
        return ResponseEntity.ok(authors);
    }

    /**
     * Retrieves authors matching the given last name (case-insensitive).
     *
     * @param lastName the partial or full last name to search for
     * @return a list of matching AuthorDTOs
     */
    @GetMapping("/name/{lastName}")
    public ResponseEntity<List<AuthorDTO>> getAuthorsByLastName(@PathVariable String lastName) {
        List<AuthorDTO> authors = authorService.getAuthorsByLastName(lastName);
        return ResponseEntity.ok(authors);
    }

    // ==================== [ POST ] ====================

    /**
     * Creates a new author.
     *
     * @param dto the input data for the author
     * @return the created AuthorDTO with a Location header
     */
    @PostMapping
    public ResponseEntity<AuthorDTO> addAuthor(@RequestBody AuthorInputDTO dto) {
        AuthorDTO savedDto = authorService.addAuthor(dto);
        return ResponseEntity
                .created(URI.create("/authors/" + savedDto.getAuthorId()))
                .body(savedDto);
    }

}