package dev.jonas.library.controllers.api;

import dev.jonas.library.dtos.author.AuthorDTO;
import dev.jonas.library.dtos.author.AuthorInputDTO;
import dev.jonas.library.services.author.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


/**
 * REST controller for managing authors in the library system.
 * Provides endpoints to retrieve and create authors.
 */
@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    // ==================== [ GET ] ====================
    @GetMapping
    public ResponseEntity<List<AuthorDTO>> getAllAuthors() {
        List<AuthorDTO> authors = authorService.getAllAuthorDTOs();
        return ResponseEntity.ok(authors);
    }

    @GetMapping("/name/{lastName}")
    public ResponseEntity<List<AuthorDTO>> getAuthorsByLastName(@PathVariable String lastName) {
        List<AuthorDTO> authors = authorService.getAuthorsByLastName(lastName);
        return ResponseEntity.ok(authors);
    }

    // ==================== [ POST ] ====================

    /**
     * Admin-only endpoint for adding new Authors to the library.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthorDTO> addAuthor(@RequestBody @Valid AuthorInputDTO dto) {
        AuthorDTO savedDto = authorService.addAuthor(dto);
        return ResponseEntity
                .created(URI.create("/authors/" + savedDto.getAuthorId()))
                .body(savedDto);
    }
}
