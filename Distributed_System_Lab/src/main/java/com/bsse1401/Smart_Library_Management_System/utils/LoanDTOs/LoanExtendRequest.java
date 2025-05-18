package com.bsse1401.Smart_Library_Management_System.utils.LoanDTOs;

import lombok.Data;

@Data
public class LoanExtendRequest {
    private Long loanId;
    private Long userId;
    private int extensionDays;
}

