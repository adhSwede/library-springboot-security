package dev.jonas.library.services.auth;

import dev.jonas.library.dtos.auth.AuthResponseDto;
import dev.jonas.library.dtos.auth.LoginRequestDto;
import dev.jonas.library.dtos.auth.RegisterRequestDto;

public interface AuthService {
    AuthResponseDto login(LoginRequestDto loginDto);

    AuthResponseDto register(RegisterRequestDto registerDto);

    AuthResponseDto refresh(String oldRefreshToken);

    void logoutAuthenticatedUser();
}
