package com.bsse1401.loan_service.service;

import com.bsse1401.loan_service.model.Book;
import com.bsse1401.loan_service.model.Loan;
import com.bsse1401.loan_service.model.User;
import com.bsse1401.loan_service.repository.LoanRepository;
import com.bsse1401.loan_service.utils.BookUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepo;
    private final RestTemplate restTemplate;

    @Value("${user.service.url}")
    private String userServiceUrl;

    @Value("${book.service.url}")
    private String bookServiceUrl;

    public Loan issueBook(Long userId, Long bookId, LocalDateTime dueDate) {
        User user = restTemplate.getForObject(userServiceUrl + "/api/user_service/users/" + userId, User.class);
        Book book = restTemplate.getForObject(bookServiceUrl + "/api/book_service/books/" + bookId, Book.class);

        if (book == null || user == null) {
            throw new IllegalArgumentException("Invalid user or book ID");
        }

        if (book.getAvailableCopies() <= 0) {
            throw new IllegalStateException("No available copies to loan");
        }

        // Update book availability
        BookUpdateRequest updateRequest = new BookUpdateRequest();
        updateRequest.setCopies(book.getCopies());
        updateRequest.setAvailableCopies(book.getAvailableCopies() - 1);

        restTemplate.put(bookServiceUrl + "/api/book_service/books/update/" + bookId, updateRequest);

        Loan loan = new Loan();
        loan.setUserId(userId);
        loan.setBookId(bookId);
        loan.setDueDate(dueDate);

        return loanRepo.save(loan);
    }

    public Loan returnBook(Long loanId, Long userId) {
        Loan loan = loanRepo.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid loan ID"));

        if (loan.getStatus() != Loan.Status.ACTIVE) {
            throw new IllegalStateException("Loan already returned");
        }

        if (!Objects.equals(loan.getUserId(), userId)) {
            throw new IllegalStateException("You did not perform that loan");
        }

        Book book = restTemplate.getForObject(bookServiceUrl + "/api/book_service/books/" + loan.getBookId(), Book.class);
        if (book == null) {
            throw new IllegalStateException("Book not found when returning");
        }

        loan.setStatus(Loan.Status.RETURNED);
        loan.setReturnDate(LocalDateTime.now());

        // Increment available copies
        BookUpdateRequest updateRequest = new BookUpdateRequest();
        updateRequest.setCopies(book.getCopies());
        updateRequest.setAvailableCopies(book.getAvailableCopies() + 1);

        restTemplate.put(bookServiceUrl + "/api/book_service/books/update/" + book.getBookId(), updateRequest);

        return loanRepo.save(loan);
    }

    public Loan extendLoan(Long userId, Long loanId, int days) {
        Loan loan = loanRepo.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid loan ID"));

        if (loan.getStatus() != Loan.Status.ACTIVE) {
            throw new IllegalStateException("Cannot extend a returned loan");
        }

        if (!Objects.equals(loan.getUserId(), userId)) {
            throw new IllegalStateException("You did not perform that loan");
        }

        loan.setDueDate(loan.getDueDate().plusDays(days));
        loan.setExtensionsCount(loan.getExtensionsCount() + 1);

        return loanRepo.save(loan);
    }

    public List<Loan> getLoansForUser(Long userId) {
        return loanRepo.findByUserId(userId);
    }

    public List<Loan> getOverdueLoans() {
        return loanRepo.findByDueDateBeforeAndStatus(LocalDateTime.now(), Loan.Status.ACTIVE);
    }

    public List<Loan> findAll() {
        return loanRepo.findAll();
    }
}
