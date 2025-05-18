package com.bsse1401.book_service.utils;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class BookUpdateRequest {

    @Min(value = 0, message = "Copies can't be negative")
    private Integer copies;

    @Min(value = 0, message = "Available copies can't be negative")
    private Integer availableCopies;
}

