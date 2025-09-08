package dev.jonas.library.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "loans")
@Getter
@Setter
@NoArgsConstructor
public class Loan {
    // ==================== [ Id ] ====================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loanId;

    // ==================== [ Dates ] ====================
    @Column(nullable = false)
    private LocalDate borrowedDate;

    @Column(nullable = false)
    private LocalDate dueDate;

    // ==================== [ Return Tracking ] ====================
    private LocalDate returnedDate;
    // ==================== [ Relationships ] ====================
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    // ==================== [ Custom Constructor ] ====================
    public Loan(User user, Book book, LocalDate borrowedDate, LocalDate dueDate) {
        this.user = user;
        this.book = book;
        this.borrowedDate = borrowedDate;
        this.dueDate = dueDate;
    }

    // ==================== [ Checks ] ====================
    public boolean isReturned() {
        return returnedDate != null;
    }
}