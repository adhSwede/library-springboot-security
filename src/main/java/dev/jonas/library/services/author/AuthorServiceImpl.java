package dev.jonas.library.services.author;

import dev.jonas.library.dtos.author.AuthorDTO;
import dev.jonas.library.dtos.author.AuthorInputDTO;
import dev.jonas.library.entities.Author;
import dev.jonas.library.mappers.DtoToEntityMapper;
import dev.jonas.library.mappers.EntityToDtoMapper;
import dev.jonas.library.repositories.AuthorRepository;
import dev.jonas.library.utils.InputValidator;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation for managing authors.
 * Provides functionality to add authors and retrieve them by filters.
 */
@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    // #################### [ Constructor ] ####################

    /**
     * Constructs the AuthorServiceImpl with the given repository.
     *
     * @param authorRepository repository for Author entities
     */
    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    // #################### [ GET ] ####################

    /**
     * Retrieves all authors in the system.
     *
     * @return a list of AuthorDTOs
     */
    @Override
    public List<AuthorDTO> getAllAuthorDTOs() {
        return authorRepository.findAll().stream()
                .map(EntityToDtoMapper::mapToAuthorDto)
                .toList();
    }

    /**
     * Retrieves authors whose last name contains the given keyword (case-insensitive).
     *
     * @param lastName keyword to search by
     * @return list of matching AuthorDTOs
     */
    @Override
    public List<AuthorDTO> getAuthorsByLastName(String lastName) {
        return authorRepository
                .findByLastNameContainingIgnoreCase(lastName)
                .stream()
                .map(EntityToDtoMapper::mapToAuthorDto)
                .toList();
    }

    // #################### [ POST ] ####################

    /**
     * Adds a new author to the database after validating input.
     *
     * @param dto author input data
     * @return the saved author as AuthorDTO
     */
    @Override
    public AuthorDTO addAuthor(AuthorInputDTO dto) {
        InputValidator.requireNonBlank(dto.getFirstName(), "First name");
        InputValidator.requireNonBlank(dto.getLastName(), "Last name");
        InputValidator.requirePositive(dto.getBirthYear(), "Birth year");
        InputValidator.requireNonBlank(dto.getNationality(), "Nationality");

        Author author = DtoToEntityMapper.mapToAuthorEntity(dto);
        Author savedAuthor = authorRepository.save(author);
        return EntityToDtoMapper.mapToAuthorDto(savedAuthor);
    }

}