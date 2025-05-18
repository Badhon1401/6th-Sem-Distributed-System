package com.bsse1401.Smart_Library_Management_System.service;

import com.bsse1401.Smart_Library_Management_System.model.Book;
import com.bsse1401.Smart_Library_Management_System.model.Loan;
import com.bsse1401.Smart_Library_Management_System.model.User;
import com.bsse1401.Smart_Library_Management_System.repository.LoanRepository;
import com.bsse1401.Smart_Library_Management_System.utils.BookDTOs.BookUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepo;
    private final UserService userService;
    private final BookService bookService;

    public Loan issueBook(Long userId, Long bookId, LocalDateTime dueDate) {
        User user = userService.getUser(userId);

        Book book = bookService.getBook(bookId);

        if (book.getAvailableCopies() <= 0) {
            throw new IllegalStateException("No available copies to loan");
        }
        BookUpdateRequest bookUpdateRequest=new BookUpdateRequest();
        bookUpdateRequest.setCopies(book.getCopies());
        bookUpdateRequest.setAvailableCopies(book.getAvailableCopies() - 1);
        bookService.updateBook(bookId,bookUpdateRequest);

        Loan loan = new Loan();
        loan.setUser(user);
        loan.setBook(book);
        loan.setDueDate(dueDate);
        return loanRepo.save(loan);
    }

    public Loan returnBook(Long loanId,Long userId) {
        Optional<Loan> loanOpt = loanRepo.findById(loanId);
        Loan loan = loanOpt.orElseThrow(() -> new IllegalArgumentException("Invalid loan ID"));

        if (loan.getStatus() != Loan.Status.ACTIVE) {
            throw new IllegalStateException("Loan already returned");
        }
        if (!Objects.equals(loan.getUser().getId(), userId)) {
            throw new IllegalStateException("You did not performed that loan");
        }

        loan.setStatus(Loan.Status.RETURNED);
        loan.setReturnDate(LocalDateTime.now());

        Book book = loan.getBook();
        BookUpdateRequest bookUpdateRequest=new BookUpdateRequest();
        bookUpdateRequest.setCopies(book.getCopies());
        bookUpdateRequest.setAvailableCopies(book.getAvailableCopies() + 1);

        bookService.updateBook(book.getBookId(),bookUpdateRequest);

        return loanRepo.save(loan);
    }

    public List<Loan> getLoansForUser(Long userId) {
        return loanRepo.findByUserId(userId);
    }

    public List<Loan> getOverdueLoans() {
        return loanRepo.findByDueDateBeforeAndStatus(LocalDateTime.now(), Loan.Status.ACTIVE);
    }

    public Loan extendLoan(Long userId,Long loanId, int days) {
        Loan loan = loanRepo.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid loan ID"));

        if (loan.getStatus() != Loan.Status.ACTIVE) {
            throw new IllegalStateException("Cannot extend a returned loan");
        }

        if (!Objects.equals(loan.getUser().getId(), userId)) {
            throw new IllegalStateException("You did not performed that loan");
        }

        loan.setDueDate(loan.getDueDate().plusDays(days));
        loan.setExtensionsCount(loan.getExtensionsCount() + 1);
        return loanRepo.save(loan);
    }

    public List<Loan> findAll() {
        return loanRepo.findAll();
    }
}
