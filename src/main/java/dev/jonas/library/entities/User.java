package dev.jonas.library.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    // #################### [ Id ] ####################
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    // #################### [ Basic Info ] ####################
    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    private LocalDate registrationDate;

    // #################### [ Lifecycle Hooks ] ####################
    @PrePersist
    protected void onCreate() {
        registrationDate = LocalDate.now();
    }

    // #################### [ Custom Constructor ] ####################
    public User(String firstName, String lastName, String email, String password, LocalDate registrationDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.registrationDate = registrationDate;
    }

    // #################### [ Relationships ] ####################
    // This entity is referenced by Loan.
}