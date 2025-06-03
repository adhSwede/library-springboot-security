package dev.jonas.library.services;

import dev.jonas.library.dtos.user.UserDTO;
import dev.jonas.library.dtos.user.UserInputDTO;

import java.util.List;

public interface UserService {
    // #################### [ GET ] ####################
    List<UserDTO> getAllUserDTOs();

    UserDTO getUserByEmail(String email);

    // #################### [ POST ] ####################
    UserDTO addUser(UserInputDTO dto);

}
