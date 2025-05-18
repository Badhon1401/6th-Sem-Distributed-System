package com.bsse1401.loan_service.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
public class LoanRequest {
    private Long userId;
    private Long bookId;
    private LocalDateTime dueDate;
}
