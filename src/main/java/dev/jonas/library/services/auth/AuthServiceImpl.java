package dev.jonas.library.services.auth;

import dev.jonas.library.dtos.auth.AuthResponseDto;
import dev.jonas.library.dtos.auth.LoginRequestDto;
import dev.jonas.library.dtos.user.UserInputDTO;
import dev.jonas.library.entities.RefreshToken;
import dev.jonas.library.entities.Role;
import dev.jonas.library.entities.User;
import dev.jonas.library.entities.UserRole;
import dev.jonas.library.exceptions.auth.EmailAlreadyUsedException;
import dev.jonas.library.mappers.DtoToEntityMapper;
import dev.jonas.library.mappers.RolesToAuthorityMapper;
import dev.jonas.library.repositories.RoleRepository;
import dev.jonas.library.repositories.UserRepository;
import dev.jonas.library.repositories.UserRoleRepository;
import dev.jonas.library.security.CustomUserDetails;
import dev.jonas.library.security.jwt.JwtUtil;
import dev.jonas.library.utils.EntityFetcher;
import lombok.RequiredArgsConstructor;
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

    // #################### [ Login ] ####################
    @Override
    public AuthResponseDto login(LoginRequestDto loginDto) {
        User user = EntityFetcher.getUserOrThrow(loginDto.getEmail(), userRepository);

        if (loginAttemptService.isLocked(user)) {
            throw new RuntimeException("Account is temporarily locked. Please try again later.");
        }

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            loginAttemptService.recordFailedAttempt(user);
            throw new RuntimeException("Invalid email or password");
        }

        loginAttemptService.reset(user);

        return generateAuthTokensForUser(user);
    }

    // #################### [ Register ] ####################
    @Override
    public AuthResponseDto register(UserInputDTO registerDto) {
        boolean exists = userRepository.findByEmailIgnoreCase(registerDto.getEmail()).isPresent();

        if (exists) {
            throw new EmailAlreadyUsedException("Email is already taken");
        }

        User user = DtoToEntityMapper.mapToUserEntity(registerDto);
        User savedUser = userRepository.save(user);

        Role role = EntityFetcher.getRoleOrThrow("USER", roleRepository);

        UserRole userRole = new UserRole(user.getUserId(), role.getRoleId());
        userRoleRepository.save(userRole);

        return generateAuthTokensForUser(user);
    }

    // #################### [ Refresh Token ] ####################
    @Override
    public AuthResponseDto refresh(String oldRefreshToken) {
        RefreshToken storedToken = refreshTokenService.validateAndGetToken(oldRefreshToken);
        User user = storedToken.getUser();

        return generateAuthTokensForUser(user);
    }

    // #################### [ Logout ] ####################
    @Override
    public void logoutAuthenticatedUser() {
        User user = EntityFetcher.getUserOrThrow(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                userRepository
        );

        refreshTokenService.revokeAllTokensForUser(user);
    }

    // #################### [ Helper Methods ] ####################
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