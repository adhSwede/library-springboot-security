package dev.jonas.library.services.user;

import dev.jonas.library.dtos.user.UserDTO;
import dev.jonas.library.dtos.user.UserInputDTO;
import dev.jonas.library.entities.User;
import dev.jonas.library.exceptions.UserNotFoundException;
import dev.jonas.library.mappers.DtoToEntityMapper;
import dev.jonas.library.mappers.EntityToDtoMapper;
import dev.jonas.library.repositories.UserRepository;
import dev.jonas.library.utils.InputValidator;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation for managing users in the library system.
 * Supports adding users, retrieving all users, and searching by email.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    // #################### [ Constructor ] ####################

    /**
     * Constructs the UserServiceImpl with the required user repository.
     *
     * @param userRepository the repository for User entities
     */
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // #################### [ GET ] ####################

    /**
     * Retrieves all users in the system.
     *
     * @return a list of UserDTOs
     */
    @Override
    public List<UserDTO> getAllUserDTOs() {
        return userRepository.findAll().stream()
                .map(EntityToDtoMapper::mapToUserDto)
                .toList();
    }

    /**
     * Retrieves a user by their email address.
     *
     * @param email the user's email
     * @return the corresponding UserDTO
     * @throws UserNotFoundException if no user with the given email is found
     */
    @Override
    public UserDTO getUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .map(EntityToDtoMapper::mapToUserDto)
                .orElseThrow(() -> new UserNotFoundException("No user found with email: " + email));
    }

    // #################### [ POST ] ####################

    /**
     * Adds a new user to the system.
     *
     * @param dto the input data for the new user
     * @return the saved user as a UserDTO
     */
    @Override
    public UserDTO addUser(UserInputDTO dto) {
        InputValidator.requireNonBlank(dto.getFirstName(), "First name");
        InputValidator.requireNonBlank(dto.getLastName(), "Last name");
        InputValidator.requireNonBlank(dto.getEmail(), "Email");

        User user = DtoToEntityMapper.mapToUserEntity(dto);
        User savedUser = userRepository.save(user);
        return EntityToDtoMapper.mapToUserDto(savedUser);
    }
}
