package dev.jonas.library.controllers.api;

import dev.jonas.library.dtos.loan.LoanDTO;
import dev.jonas.library.dtos.user.UserDTO;
import dev.jonas.library.dtos.user.UserInputDTO;
import dev.jonas.library.services.loan.LoanService;
import dev.jonas.library.services.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * REST controller for handling user-related operations.
 * Provides endpoints to create users, fetch users by email,
 * list all users, and get loans associated with a specific user.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final LoanService loanService;

    // ==================== [ GET ] ====================
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUserDTOs();
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("@userAccessValidator.isAdminOrSelf(#email)")
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        UserDTO userDto = userService.getUserByEmail(email);
        return ResponseEntity.ok(userDto);
    }

    @PreAuthorize("@userAccessValidator.isAdminOrSelf(#userId)")
    @GetMapping("/{userId}/loans")
    public ResponseEntity<List<LoanDTO>> getLoansByUserId(@PathVariable Long userId) {
        List<LoanDTO> loans = loanService.getLoansByUserId(userId);
        return ResponseEntity.ok(loans);
    }

    // ==================== [ POST ] ====================
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UserDTO> addUser(@RequestBody @Valid UserInputDTO dto) {
        UserDTO savedUser = userService.addUser(dto);
        return ResponseEntity
                .created(URI.create("/users/email/" + savedUser.getEmail()))
                .body(savedUser);
    }
}