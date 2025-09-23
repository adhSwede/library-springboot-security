package dev.jonas.library.entities;

import dev.jonas.library.utils.LocalDateTimeConverter;
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

    // ==================== [ Ids ] ====================
    @Id
    private long userId;

    @Id
    private long roleId;

    // ==================== [ Relationships ] ====================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roleId", insertable = false, updatable = false)
    private Role role;

    // ==================== [ Timestamps ] ====================
    @Column(name = "assigned_date", nullable = true)
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime assignedDate;

    // ==================== [ Constructors ] ====================
    public UserRole(long userId, long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    // ==================== [ Life cycle hooks ] ====================
    @PrePersist
    protected void onCreate() {
        assignedDate = LocalDateTime.now();
    }

    // ==================== [ Static Key Class ] ====================
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