package dev.jonas.library.entities;

import dev.jonas.library.security.encryption.StringEncryptionConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor

public class User {
    // ==================== [ Id ] ====================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    // ==================== [ Basic Info ] ====================
    // Email is encrypted using AES before storage
    @Convert(converter = StringEncryptionConverter.class)
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false) // Password is hashed using BCrypt before storage
    private String password;

    @Column(nullable = false)
    private LocalDateTime registrationDate;

    // ==================== [ Additions for Spring security ] ====================
    @Column(nullable = false)
    private boolean enabled;

    @Column(nullable = false)
    private boolean accountNonExpired;

    @Column(nullable = false)
    private boolean accountNonLocked;

    @Column(nullable = false)
    private boolean credentialsNonExpired;

    @Column(nullable = false)
    private long failedLoginAttempts;

    @Column
    private LocalDateTime lastLoginAttempt;

    @Column
    private LocalDateTime lockedUntil;

    // ==================== [ Custom Constructor ] ====================
    public User(String firstName, String lastName, String email, String password, LocalDateTime registrationDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.registrationDate = registrationDate;
        this.enabled = true;
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.failedLoginAttempts = 0;
        this.lastLoginAttempt = null;
        this.lockedUntil = null;
    }

    // ==================== [ Life cycle Hooks ] ====================
    @PrePersist
    protected void onCreate() {
        registrationDate = LocalDateTime.now();
    }

    // ==================== [ Relationships ] ====================
    // This entity is referenced by Loan.
}