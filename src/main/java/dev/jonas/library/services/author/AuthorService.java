package dev.jonas.library.services.author;

import dev.jonas.library.dtos.author.AuthorDTO;
import dev.jonas.library.dtos.author.AuthorInputDTO;

import java.util.List;

public interface AuthorService {
    List<AuthorDTO> getAllAuthorDTOs();

    List<AuthorDTO> getAuthorsByLastName(String lastName);

    AuthorDTO addAuthor(AuthorInputDTO dto);
}