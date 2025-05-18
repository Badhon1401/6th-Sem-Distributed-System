package com.bsse1401.stats_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Loan {


    private Long id;

    private Long userId;
    private Long bookId;
    private LocalDateTime issueDate;
    private LocalDateTime dueDate;

    private LocalDateTime returnDate;
    private Status status = Status.ACTIVE;


    private int extensionsCount = 0;


    private LocalDateTime createdAt;


    private LocalDateTime updatedAt;


    public void onCreate() {
        this.issueDate = LocalDateTime.now();
        if (status == null) {
            status = Status.ACTIVE;
        }
    }

    public enum Status {
        ACTIVE,
        RETURNED
    }
}
