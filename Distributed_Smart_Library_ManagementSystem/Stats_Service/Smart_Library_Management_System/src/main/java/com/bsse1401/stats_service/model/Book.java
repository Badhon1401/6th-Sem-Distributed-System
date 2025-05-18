package com.bsse1401.stats_service.model;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    private Long bookId;

    private String title;

    private String author;


    private String isbn;


    private int copies;


    private int availableCopies;


    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;



    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.availableCopies > this.copies) {
            this.availableCopies = this.copies;
        }
    }


    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        if (this.availableCopies > this.copies) {
            this.availableCopies = this.copies;
        }
    }
}
