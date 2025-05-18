package com.bsse1401.loan_service.repository;

import com.bsse1401.loan_service.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    Optional<Loan> findById(Long loanId);
    List<Loan> findByUserId(Long userId);
    List<Loan> findByDueDateBeforeAndStatus(LocalDateTime now, Loan.Status status);
}

