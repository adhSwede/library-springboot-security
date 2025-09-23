package dev.jonas.library.services.user;

import dev.jonas.library.dtos.user.RoleChangeRequest;
import dev.jonas.library.dtos.user.UserDTO;
import dev.jonas.library.dtos.user.UserInputDTO;
import dev.jonas.library.entities.Role;
import dev.jonas.library.entities.User;
import dev.jonas.library.entities.UserRole;
import dev.jonas.library.mappers.DtoToEntityMapper;
import dev.jonas.library.mappers.EntityToDtoMapper;
import dev.jonas.library.mappers.RolesToAuthorityMapper;
import dev.jonas.library.repositories.RoleRepository;
import dev.jonas.library.repositories.UserRepository;
import dev.jonas.library.repositories.UserRoleRepository;
import dev.jonas.library.security.UserAccessValidator;
import dev.jonas.library.utils.EntityFetcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation for managing users.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RolesToAuthorityMapper rolesToAuthorityMapper;
    private final UserAccessValidator userAccessValidator;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

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
    //Deprecated: User registration is handled in AuthService
    @Override
    public UserDTO addUser(UserInputDTO dto) {
        User user = DtoToEntityMapper.mapToUserEntity(dto);
        User savedUser = userRepository.save(user);
        return EntityToDtoMapper.mapToUserDto(savedUser);
    }

    @Override
    public UserDTO assignRole(RoleChangeRequest request) {
        User editor = userAccessValidator.getCurrentAuthenticatedUser();
        userAccessValidator.validateUserAccess(editor.getUserId());

        User targetUser = EntityFetcher.getUserOrThrow(request.getEmail(), userRepository);
        Role role = EntityFetcher.getRoleOrThrow(request.getRoleName(), roleRepository);

        boolean alreadyHasRole = userRoleRepository.existsByUserIdAndRoleId(
                targetUser.getUserId(), role.getRoleId()
        );

        if (!alreadyHasRole) {
            userRoleRepository.save(new UserRole(targetUser.getUserId(), role.getRoleId()));
            log.info("Role '{}' assigned to user '{}' by '{}'",
                    role.getRoleName(), targetUser.getEmail(), editor.getEmail());
        } else {
            log.info("User '{}' already has role '{}'",
                    targetUser.getEmail(), role.getRoleName());
        }

        return EntityToDtoMapper.mapToUserDto(targetUser);
    }


    @Override
    public UserDTO removeRole(RoleChangeRequest request) {
        User editor = userAccessValidator.getCurrentAuthenticatedUser();
        userAccessValidator.validateUserAccess(editor.getUserId());

        User targetUser = EntityFetcher.getUserOrThrow(request.getEmail(), userRepository);
        Role role = EntityFetcher.getRoleOrThrow(request.getRoleName(), roleRepository);

        userRoleRepository.deleteByUserIdAndRoleId(targetUser.getUserId(), role.getRoleId());
        log.info("Role '{}' removed from user '{}' by '{}'",
                role.getRoleName(), targetUser.getEmail(), editor.getEmail());

        return EntityToDtoMapper.mapToUserDto(targetUser);
    }
    
}