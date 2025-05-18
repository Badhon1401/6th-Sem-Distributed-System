package com.bsse1401.loan_service.utils;

import lombok.Data;

@Data
public class LoanReturnRequest {
    private Long loanId;
    private Long userId;
}
