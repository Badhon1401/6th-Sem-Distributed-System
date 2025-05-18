package com.bsse1401.Smart_Library_Management_System.utils.LoanDTOs;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LoanRequest {
    private Long userId;
    private Long bookId;
    private LocalDateTime dueDate;
}
