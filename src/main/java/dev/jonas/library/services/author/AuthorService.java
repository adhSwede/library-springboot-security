package dev.jonas.library.services.author;

import dev.jonas.library.dtos.author.AuthorDTO;
import dev.jonas.library.dtos.author.AuthorInputDTO;

import java.util.List;

public interface AuthorService {
    // #################### [ GET ] ####################
    List<AuthorDTO> getAllAuthorDTOs();

    List<AuthorDTO> getAuthorsByLastName(String lastName);

    // #################### [ POST ] ####################
    AuthorDTO addAuthor(AuthorInputDTO dto);
}