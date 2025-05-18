package com.bsse1401.Smart_Library_Management_System.controller;

import com.bsse1401.Smart_Library_Management_System.model.Loan;
import com.bsse1401.Smart_Library_Management_System.service.LoanService;
import com.bsse1401.Smart_Library_Management_System.utils.LoanDTOs.LoanExtendRequest;
import com.bsse1401.Smart_Library_Management_System.utils.LoanDTOs.LoanRequest;
import com.bsse1401.Smart_Library_Management_System.utils.LoanDTOs.LoanReturnRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping("/issue")
    public ResponseEntity<Loan> issueBook(@RequestBody LoanRequest request) {
        return ResponseEntity.ok(loanService.issueBook(request.getUserId(), request.getBookId(), request.getDueDate()));
    }

    @PostMapping("/returns")
    public ResponseEntity<Loan> returnBook(@RequestBody LoanReturnRequest request) {
        return ResponseEntity.ok(loanService.returnBook(request.getLoanId(),request.getUserId()));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Loan>> getUserLoans(@PathVariable Long userId) {
        return ResponseEntity.ok(loanService.getLoansForUser(userId));
    }
    @GetMapping("/debug/loans")
    public List<Loan> getAllLoans() {
        return loanService.findAll();
    }


    @GetMapping("/overdue")
    public ResponseEntity<List<Loan>> getOverdues() {
        return ResponseEntity.ok(loanService.getOverdueLoans());
    }

    @PutMapping("/{loanId}/extend")
    public ResponseEntity<Loan> extendLoan(@PathVariable Long loanId, @RequestBody LoanExtendRequest request) {
        return ResponseEntity.ok(loanService.extendLoan(request.getUserId(),loanId, request.getExtensionDays()));
    }
}
