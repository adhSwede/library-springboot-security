package dev.jonas.library.entities;

import dev.jonas.library.security.encryption.StringEncryptionConverter;
import dev.jonas.library.utils.LocalDateTimeConverter;
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
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    // Hashed using BCrypt before storage
    @Column(nullable = false)
    @Convert(converter = StringEncryptionConverter.class)
    private String password;

    // Encrypted using AES before storage
    @Column(nullable = false)
    @Convert(converter = StringEncryptionConverter.class)
    private String nationalId;

    @Column(nullable = false)
    @Convert(converter = LocalDateTimeConverter.class)
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
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime lastLoginAttempt;

    @Column
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime lockedUntil;

    // ==================== [ Custom Constructor ] ====================
    public User(String firstName, String lastName, String email, String password, String nationalId, LocalDateTime registrationDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.nationalId = nationalId;
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