package dev.jonas.library.services.auth;

import dev.jonas.library.dtos.auth.AuthResponseDto;
import dev.jonas.library.dtos.auth.LoginRequestDto;
import dev.jonas.library.dtos.auth.RegisterRequestDto;
import dev.jonas.library.entities.Role;
import dev.jonas.library.entities.User;
import dev.jonas.library.entities.UserRole;
import dev.jonas.library.repositories.RoleRepository;
import dev.jonas.library.repositories.UserRepository;
import dev.jonas.library.repositories.UserRoleRepository;
import dev.jonas.library.security.jwt.JwtUtil;
import lombok.AllArgsConstructor;
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

    @Override
    public AuthResponseDto login(LoginRequestDto loginDto) {
        User user = userRepository.findByEmailIgnoreCase(loginDto.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getEmail());

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

        User user = new User();
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user = userRepository.save(user); // persist and retrieve the generated ID

        Role role = roleRepository.findByRoleName("USER");
        if (role == null) {
            throw new RuntimeException("Default USER role not found");
        }

        UserRole userRole = new UserRole(user.getUserId(), role.getRoleId());
        userRoleRepository.save(userRole);

        String token = jwtUtil.generateToken(user.getEmail());

        return new AuthResponseDto(
                LocalDateTime.now().plusHours(1),
                token
                // "Bearer" token type removed for simplicity
        );
    }
}
