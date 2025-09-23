package dev.jonas.library.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for role change requests.
 * Contains the email of the user and the role name to be assigned or removed.
 */
@Getter
@Setter
@AllArgsConstructor
public class RoleChangeRequest {
    private String email;
    private String roleName;
}
