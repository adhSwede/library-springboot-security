package dev.jonas.library.services.user;

import dev.jonas.library.dtos.user.UserDTO;
import dev.jonas.library.dtos.user.UserInputDTO;
import dev.jonas.library.entities.User;
import dev.jonas.library.exceptions.UserNotFoundException;
import dev.jonas.library.mappers.DtoToEntityMapper;
import dev.jonas.library.mappers.EntityToDtoMapper;
import dev.jonas.library.mappers.RolesToAuthorityMapper;
import dev.jonas.library.repositories.UserRepository;
import dev.jonas.library.utils.InputValidator;
import dev.jonas.library.utils.UserAccessValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation for managing users.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RolesToAuthorityMapper rolesToAuthorityMapper;
    private final UserAccessValidator userAccessValidator;

    // ==================== [ GET ] ====================
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
        User targetUser = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UserNotFoundException("No user found with email: " + email));

        userAccessValidator.validateUserAccess(targetUser.getUserId()); // Validate access

        return EntityToDtoMapper.mapToUserDto(targetUser);
    }

    // ==================== [ POST ] ====================
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