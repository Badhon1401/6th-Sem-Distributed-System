package com.bsse1401.loan_service.controller;

import com.bsse1401.loan_service.model.Loan;
import com.bsse1401.loan_service.service.LoanService;
import com.bsse1401.loan_service.utils.LoanExtendRequest;
import com.bsse1401.loan_service.utils.LoanRequest;
import com.bsse1401.loan_service.utils.LoanReturnRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loan_service/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping("/issue")
    public ResponseEntity<Loan> issueBook(@RequestBody LoanRequest request) {
        return ResponseEntity.ok(
                loanService.issueBook(request.getUserId(), request.getBookId(), request.getDueDate())
        );
    }

    @PostMapping("/returns")
    public ResponseEntity<Loan> returnBook(@RequestBody LoanReturnRequest request) {
        return ResponseEntity.ok(
                loanService.returnBook(request.getLoanId(), request.getUserId())
        );
    }

    @PutMapping("/{loanId}/extend")
    public ResponseEntity<Loan> extendLoan(@PathVariable Long loanId, @RequestBody LoanExtendRequest request) {
        return ResponseEntity.ok(
                loanService.extendLoan(request.getUserId(), loanId, request.getExtensionDays())
        );
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Loan>> getUserLoans(@PathVariable Long userId) {
        return ResponseEntity.ok(loanService.getLoansForUser(userId));
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<Loan>> getOverdues() {
        return ResponseEntity.ok(loanService.getOverdueLoans());
    }

    @GetMapping("/all")
    public ResponseEntity<List<Loan>> getAllLoans() {
        return ResponseEntity.ok(loanService.findAll());
    }
}
