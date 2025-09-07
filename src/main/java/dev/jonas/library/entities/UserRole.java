package dev.jonas.library.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

// Indexes for efficient querying on userId and roleId
@Table(name = "user_roles",
        indexes = {
                @Index(name = "idx_user_roles_user_id", columnList = "userId"),
                @Index(name = "idx_user_roles_role_id", columnList = "roleId")
        })
@IdClass(UserRole.UserRoleId.class)
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {
    // #################### [ Ids ] ####################
    @Id
    private long userId;

    @Id
    private long roleId;

    @Column(nullable = true) // Allow null for SQLite compatibility.
    private LocalDateTime createdDate;
    // #################### [ Relationships ] ####################
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roleId", insertable = false, updatable = false)
    private Role role;

    // #################### [ Constructors ] ####################
    // w/o createdDate
    public UserRole(long userId, long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    // #################### [ Life cycle hooks ] ####################
    @PrePersist
    protected void onCreate() {
        if (createdDate == null) {
            createdDate = LocalDateTime.now();
        }
    }

    // #################### [ Static Key Class ] ####################
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class UserRoleId implements Serializable {
        private long userId;
        private long roleId;
    }
}