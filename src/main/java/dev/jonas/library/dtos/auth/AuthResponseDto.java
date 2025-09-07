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
    @NotNull
    private LocalDateTime expiresAt;

    @NotBlank
    private String token;
    
//  Removed Token type because it was redundant.
//  private String tokenType = "Bearer";
}