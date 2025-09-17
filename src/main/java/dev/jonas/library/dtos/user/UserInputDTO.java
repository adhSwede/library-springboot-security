package dev.jonas.library.dtos.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * DTO used when registering or creating a new user.
 * Includes user credentials like password.
 */
@Getter
@Setter
@RequiredArgsConstructor
public class UserInputDTO {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 8, max = 100)
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,100}$",
            message = "Password must be 8–100 characters and include uppercase, lowercase, a number, and a special character."
    )
    private String password;
}