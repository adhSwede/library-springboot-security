package dev.jonas.library.services.auth;

import dev.jonas.library.dtos.auth.AuthResponseDto;
import dev.jonas.library.dtos.auth.LoginRequestDto;
import dev.jonas.library.dtos.user.UserInputDTO;
import dev.jonas.library.entities.RefreshToken;
import dev.jonas.library.entities.Role;
import dev.jonas.library.entities.User;
import dev.jonas.library.entities.UserRole;
import dev.jonas.library.exceptions.api.UserNotFoundException;
import dev.jonas.library.exceptions.security.AccountLockedException;
import dev.jonas.library.exceptions.security.EmailAlreadyUsedException;
import dev.jonas.library.exceptions.security.InvalidCredentialsException;
import dev.jonas.library.mappers.DtoToEntityMapper;
import dev.jonas.library.mappers.RolesToAuthorityMapper;
import dev.jonas.library.repositories.RoleRepository;
import dev.jonas.library.repositories.UserRepository;
import dev.jonas.library.repositories.UserRoleRepository;
import dev.jonas.library.security.CustomUserDetails;
import dev.jonas.library.security.jwt.JwtUtil;
import dev.jonas.library.utils.EntityFetcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Service implementation for authentication-related operations.
 * Handles user login, registration, token refreshing, and logout.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final RefreshTokenService refreshTokenService;
    private final LoginAttemptService loginAttemptService;
    private final JwtUtil jwtUtil;
    private final RolesToAuthorityMapper rolesToAuthorityMapper;

    private final Duration accessTokenDuration = Duration.ofHours(1);

    // ========== [ Login ] ==========
    @Override
    public AuthResponseDto login(LoginRequestDto loginDto) {
        User user = EntityFetcher.getUserOrThrow(loginDto.getEmail(), userRepository);
        LocalDateTime timestamp = LocalDateTime.now();

        if (loginAttemptService.isLocked(user)) {
            log.warn("User '{}' has been locked out until {}", user.getEmail(), user.getLockedUntil());
            throw new AccountLockedException("Account is temporarily locked until " + user.getLockedUntil());
        }

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            loginAttemptService.recordFailedAttempt(user);
            log.warn("Failed login attempt for email '{}' at {}", loginDto.getEmail(), timestamp);
            throw new InvalidCredentialsException("Invalid email or password");
        }

        loginAttemptService.reset(user);

        log.info("User '{}' logged in successfully at {}", user.getEmail(), timestamp);

        return generateAuthTokensForUser(user);
    }

    // ========== [ Register ] ==========
    @Override
    public AuthResponseDto register(UserInputDTO registerDto) {
        boolean exists = userRepository.findByEmailIgnoreCase(registerDto.getEmail()).isPresent();

        if (exists) {
            log.warn("Attempted registration with already taken email '{}'", registerDto.getEmail());
            throw new EmailAlreadyUsedException("Email is already taken");
        }

        User user = DtoToEntityMapper.mapToUserEntity(registerDto);
        User savedUser = userRepository.save(user);

        Role role = EntityFetcher.getRoleOrThrow("USER", roleRepository);

        UserRole userRole = new UserRole(user.getUserId(), role.getRoleId());
        userRoleRepository.save(userRole);

        log.info("New user '{}' registered successfully at {}", user.getEmail(), LocalDateTime.now());

        return generateAuthTokensForUser(user);
    }

    // ========== [ Refresh Token ] ==========
    @Override
    public AuthResponseDto refresh(String oldRefreshToken) {
        LocalDateTime timestamp = LocalDateTime.now();
        try {
            RefreshToken storedToken = refreshTokenService.validateAndGetToken(oldRefreshToken);
            User user = storedToken.getUser();

            log.info("Refresh token used successfully by user '{}' at {}", user.getEmail(), timestamp);

            return generateAuthTokensForUser(user);
        } catch (Exception e) {
            log.warn("Failed refresh token attempt at {}: {}", timestamp, e.getMessage());
            throw e; // rethrow to let your global handler catch it
        }
    }

    @Override
    public void logoutAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        LocalDateTime timestamp = LocalDateTime.now();

        try {
            User user = EntityFetcher.getUserOrThrow(email, userRepository);
            refreshTokenService.revokeAllTokensForUser(user);

            log.info("User '{}' logged out successfully at {}", user.getEmail(), timestamp);

        } catch (UserNotFoundException e) {
            log.warn("Logout attempt failed: no authenticated user found at {}", timestamp);
            throw e;
        }
    }

    // ========== [ Helper Methods ] ==========
    private AuthResponseDto generateAuthTokensForUser(User user) {
        UserDetails userDetails = new CustomUserDetails(
                user,
                rolesToAuthorityMapper.mapRolesToAuthorities(user.getUserId())
        );

        String accessToken = jwtUtil.generateToken(userDetails);
        String refreshToken = refreshTokenService.createRefreshToken(user).getToken();

        return new AuthResponseDto(
                LocalDateTime.now().plus(accessTokenDuration),
                accessToken,
                refreshToken
        );
    }
}