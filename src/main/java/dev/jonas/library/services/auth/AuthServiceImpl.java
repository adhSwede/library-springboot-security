package dev.jonas.library.services.auth;

import dev.jonas.library.dtos.auth.AuthResponseDto;
import dev.jonas.library.dtos.auth.LoginRequestDto;
import dev.jonas.library.dtos.auth.RegisterRequestDto;
import dev.jonas.library.entities.Role;
import dev.jonas.library.entities.User;
import dev.jonas.library.entities.UserRole;
import dev.jonas.library.exceptions.UserNotFoundException;
import dev.jonas.library.mappers.RolesToAuthorityMapper;
import dev.jonas.library.repositories.RoleRepository;
import dev.jonas.library.repositories.UserRepository;
import dev.jonas.library.repositories.UserRoleRepository;
import dev.jonas.library.security.CustomUserDetails;
import dev.jonas.library.security.jwt.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final RolesToAuthorityMapper rolesToAuthorityMapper;

    @Override
    public AuthResponseDto login(LoginRequestDto loginDto) {

        User user = userRepository.findByEmailIgnoreCase(loginDto.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        UserDetails userDetails = new CustomUserDetails(user, rolesToAuthorityMapper.mapRolesToAuthorities(user.getUserId()));

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(userDetails);

        return new AuthResponseDto(
                LocalDateTime.now().plusHours(1),
                token
        );
    }

    @Override
    public AuthResponseDto register(RegisterRequestDto registerDto) {
        boolean exists = userRepository.findByEmailIgnoreCase(registerDto.getEmail()).isPresent();
        if (exists) {
            throw new RuntimeException("Email is already taken");
        }

        // Create and save user
        User user = new User();
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user = userRepository.save(user);

        Role role = roleRepository.findByRoleName("USER")
                .orElseThrow(() -> new UserNotFoundException("Default USER role not found"));

        UserRole userRole = new UserRole(user.getUserId(), role.getRoleId());
        userRoleRepository.save(userRole);

        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(registerDto.getEmail());

        String token = jwtUtil.generateToken(userDetails);

        return new AuthResponseDto(
                LocalDateTime.now().plusHours(1),
                token
                // "Bearer" token type removed for simplicity
        );
    }
}
