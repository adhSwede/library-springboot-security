package dev.jonas.library.services.author;

import dev.jonas.library.dtos.author.AuthorDTO;
import dev.jonas.library.dtos.author.AuthorInputDTO;
import dev.jonas.library.entities.Author;
import dev.jonas.library.mappers.DtoToEntityMapper;
import dev.jonas.library.mappers.EntityToDtoMapper;
import dev.jonas.library.repositories.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation for managing authors.
 * Provides functionality to add authors and retrieve them by filters.
 */
@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    // #################### [ GET ] ####################
    @Override
    public List<AuthorDTO> getAllAuthorDTOs() {
        return authorRepository.findAll().stream()
                .map(EntityToDtoMapper::mapToAuthorDto)
                .toList();
    }

    @Override
    public List<AuthorDTO> getAuthorsByLastName(String lastName) {
        return authorRepository
                .findByLastNameContainingIgnoreCase(lastName)
                .stream()
                .map(EntityToDtoMapper::mapToAuthorDto)
                .toList();
    }

    // #################### [ POST ] ####################
    @Override
    public AuthorDTO addAuthor(AuthorInputDTO dto) {
        Author author = DtoToEntityMapper.mapToAuthorEntity(dto);
        Author savedAuthor = authorRepository.save(author);
        return EntityToDtoMapper.mapToAuthorDto(savedAuthor);
    }
}