package com.bsse1401.Smart_Library_Management_System.utils.LoanDTOs;

import lombok.Data;

@Data
public class LoanReturnRequest {
    private Long loanId;
    private Long userId;
}
