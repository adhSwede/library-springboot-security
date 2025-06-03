package dev.jonas.library.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    // ########## [ Id ] ##########
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    // ########## [ Basic Info ] ##########
    @Column(nullable = false, length = 200)
    private String title;

    // ########## [ Relationships ] ##########
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Author author;

    //########## [ More Info ] ##########
    @Column(nullable = false)
    private Integer publicationYear;
    private Integer availableCopies = 1;
    private Integer totalCopies = 1;

    // #################### [ Custom Constructor ] ####################
    public Book(String title, Author author, Integer publicationYear, Integer availableCopies, Integer totalCopies) {
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.availableCopies = availableCopies;
        this.totalCopies = totalCopies;
    }
}