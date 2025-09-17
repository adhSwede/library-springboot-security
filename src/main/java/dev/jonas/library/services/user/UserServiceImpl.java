package dev.jonas.library.services.user;

import dev.jonas.library.dtos.user.UserDTO;
import dev.jonas.library.dtos.user.UserInputDTO;
import dev.jonas.library.entities.User;
import dev.jonas.library.mappers.DtoToEntityMapper;
import dev.jonas.library.mappers.EntityToDtoMapper;
import dev.jonas.library.mappers.RolesToAuthorityMapper;
import dev.jonas.library.repositories.UserRepository;
import dev.jonas.library.utils.EntityFetcher;
import dev.jonas.library.utils.UserAccessValidator;
import lombok.RequiredArgsConstructor;
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
    @Override
    public List<UserDTO> getAllUserDTOs() {
        return userRepository.findAll().stream()
                .map(EntityToDtoMapper::mapToUserDto)
                .toList();
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        User targetUser = EntityFetcher.getUserOrThrow(email, userRepository);

        userAccessValidator.validateUserAccess(targetUser.getUserId()); // Validate access

        return EntityToDtoMapper.mapToUserDto(targetUser);
    }

    // ==================== [ POST ] ====================
    @Override
    public UserDTO addUser(UserInputDTO dto) {
        User user = DtoToEntityMapper.mapToUserEntity(dto);
        User savedUser = userRepository.save(user);
        return EntityToDtoMapper.mapToUserDto(savedUser);
    }
}