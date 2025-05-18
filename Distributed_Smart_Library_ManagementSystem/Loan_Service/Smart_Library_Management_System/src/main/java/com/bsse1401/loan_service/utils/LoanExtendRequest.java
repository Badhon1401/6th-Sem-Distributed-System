package com.bsse1401.loan_service.utils;

import lombok.Data;

@Data
public class LoanExtendRequest {
    private Long loanId;
    private Long userId;
    private int extensionDays;
}

