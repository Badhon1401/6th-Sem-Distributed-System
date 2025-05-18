package com.bsse1401.loan_service.utils;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BookRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String author;

    @NotBlank
    private String isbn;

    @Min(value = 1, message = "Copies must be at least 1")
    private int copies;
}

