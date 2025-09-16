package dev.jonas.library.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for authentication responses.
 */
@Getter
@Setter
@AllArgsConstructor
public class AuthResponseDto {
    private LocalDateTime expiresAt;
    private String accessToken;
    private String refreshToken;
}