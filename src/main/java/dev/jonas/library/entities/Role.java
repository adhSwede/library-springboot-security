package dev.jonas.library.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
public class Role {
    // #################### [ Id ] ####################
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    // #################### [ Basic Info ] ####################
    @Column(nullable = false, unique = true, length = 50)
    private String roleName;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    // #################### [ Life cycle Hooks ] ####################
    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
    }
}
