package dev.jonas.library.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * DTO representing a user in the system.
 * Exposes user information without sensitive data.
 */
@Getter
@Setter
@AllArgsConstructor
public class UserDTO {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime registrationDate;
}
