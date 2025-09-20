package dev.jonas.library.services.user;

import dev.jonas.library.dtos.user.UserDTO;
import dev.jonas.library.dtos.user.UserInputDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> getAllUserDTOs();

    UserDTO getUserByEmail(String email);

    UserDTO addUser(UserInputDTO dto);
}
