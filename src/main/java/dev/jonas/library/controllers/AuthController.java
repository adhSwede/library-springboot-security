package dev.jonas.library.controllers;

import dev.jonas.library.dtos.auth.AuthResponseDto;
import dev.jonas.library.dtos.auth.LoginRequestDto;
import dev.jonas.library.dtos.auth.RefreshRequestDTO;
import dev.jonas.library.dtos.auth.RegisterRequestDto;
import dev.jonas.library.services.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import dev.jonas.library.repositories.UserRepository;
import dev.jonas.library.services.auth.RefreshTokenService;

/**
 * REST controller for handling authentication-related operations such as login, registration,
 * token refresh, and logout.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody @Valid LoginRequestDto req) {
        return ResponseEntity.ok(authService.login(req));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@RequestBody @Valid RegisterRequestDto req) {
        return ResponseEntity.ok(authService.register(req));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refresh(@RequestBody RefreshRequestDTO request) {
        return ResponseEntity.ok(authService.refresh(request.getToken()));
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> logout() {
        authService.logoutAuthenticatedUser();
        return ResponseEntity.noContent().build();
    }
}