package dev.jonas.library.utils;

import dev.jonas.library.entities.User;
import dev.jonas.library.exceptions.auth.AccessDeniedException;
import dev.jonas.library.mappers.RolesToAuthorityMapper;
import dev.jonas.library.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserAccessValidator {

    private final UserRepository userRepository;
    private final RolesToAuthorityMapper rolesToAuthorityMapper;

    /**
     * Validates if the current authenticated user can access resources for the given userId.
     */
    public void validateUserAccess(Long targetUserId) {
        User currentUser = getCurrentAuthenticatedUser();

        if (isAdmin(currentUser) || isOwnResource(currentUser, targetUserId)) {
            return;
        }

        throw new AccessDeniedException("Access denied to user ID: " + targetUserId);
    }

    public User getCurrentAuthenticatedUser() {
        String currentEmail = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        return userRepository.findByEmailIgnoreCase(currentEmail)
                .orElseThrow(() -> new AccessDeniedException("Authenticated user not found"));
    }

    private boolean isAdmin(User user) {
        return rolesToAuthorityMapper
                .mapRolesToAuthorities(user.getUserId())
                .stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }

    private boolean isOwnResource(User currentUser, Long targetUserId) {
        return currentUser.getUserId().equals(targetUserId);
    }

    public boolean isAdminOrSelf(Long targetUserId) {
        User currentUser = getCurrentAuthenticatedUser();
        return isAdmin(currentUser) || isOwnResource(currentUser, targetUserId);
    }

    public boolean isAdminOrSelf(String targetUserEmail) {
        User currentUser = getCurrentAuthenticatedUser();
        Long targetUserId = userRepository.findByEmailIgnoreCase(targetUserEmail)
                .map(User::getUserId)
                .orElseThrow(() -> new AccessDeniedException("User not found with email: " + targetUserEmail));
        return isAdmin(currentUser) || isOwnResource(currentUser, targetUserId);
    }
}
