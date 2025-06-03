package dev.jonas.library.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO used when registering or creating a new user.
 * Includes user credentials like password.
 */
@Getter
@Setter
@AllArgsConstructor
public class UserInputDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
