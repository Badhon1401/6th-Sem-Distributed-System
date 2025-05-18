package com.bsse1401.book_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotBlank(message = "Author cannot be blank")
    private String author;

    @NotBlank(message = "ISBN cannot be blank")
    @Pattern(
            regexp = "^(97(8|9))?\\d{9}(\\d|X)$",
            message = "ISBN must be a valid 10 or 13 digit number"
    )
    @Column(unique = true)
    private String isbn;

    @Min(value = 1, message = "There must be at least 1 copy")
    private int copies;

    @Min(value = 0, message = "Available copies cannot be negative")
    @Max(value = Integer.MAX_VALUE, message = "Available copies cannot exceed total copies")
    private int availableCopies;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.availableCopies > this.copies) {
            this.availableCopies = this.copies;
        }
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        if (this.availableCopies > this.copies) {
            this.availableCopies = this.copies;
        }
    }
}
