package dev.jonas.library.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "authors")
@Getter
@Setter
@NoArgsConstructor
public class Author {

    // ==================== [ Id ] ====================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authorId;

    // ==================== [ Basic Info ] ====================
    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    private Integer birthYear;

    @Column(length = 100)
    private String nationality;

    // ==================== [ Custom Constructor ] ====================
    public Author(String firstName, String lastName, Integer birthYear, String nationality) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthYear = birthYear;
        this.nationality = nationality;
    }

    // ==================== [ Relationships ] ====================
    // This entity is referenced by Book.

    // ==================== [ Convenience ] ====================
    public String getFullName() {
        return firstName + " " + lastName;
    }
}