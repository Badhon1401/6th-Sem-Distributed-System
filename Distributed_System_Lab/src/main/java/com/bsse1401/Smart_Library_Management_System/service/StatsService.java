package com.bsse1401.Smart_Library_Management_System.service;

import com.bsse1401.Smart_Library_Management_System.model.Book;
import com.bsse1401.Smart_Library_Management_System.model.Loan;
import com.bsse1401.Smart_Library_Management_System.model.User;
import com.bsse1401.Smart_Library_Management_System.utils.StatsDTOs.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final BookService bookService;
    private final UserService userService;
    private final LoanService loanService;

    public List<PopularBookStats> getPopularBooks() {
        return loanService.findAll().stream()
                .collect(Collectors.groupingBy(Loan::getBook, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<Book, Long>comparingByValue().reversed())
                .limit(10)
                .map(entry -> {
                    Book b = entry.getKey();
                    PopularBookStats dto = new PopularBookStats();
                    dto.setBookId(b.getBookId());
                    dto.setTitle(b.getTitle());
                    dto.setAuthor(b.getAuthor());
                    dto.setBorrowCount(entry.getValue());
                    return dto;
                }).collect(Collectors.toList());
    }


    public List<ActiveUserStats> getActiveUsers() {
        List<Loan> allLoans = loanService.findAll();

        Map<User, List<Loan>> userLoans = allLoans.stream()
                .collect(Collectors.groupingBy(Loan::getUser));

        return userLoans.entrySet().stream()
                .map(entry -> {
                    User u = entry.getKey();
                    List<Loan> loans = entry.getValue();

                    ActiveUserStats dto = new ActiveUserStats();
                    dto.setUserId(u.getId());
                    dto.setName(u.getName());
                    dto.setBooksBorrowed((long) loans.size());
                    dto.setCurrentBorrows(loans.stream().filter(l -> l.getStatus() == Loan.Status.ACTIVE).count());
                    return dto;
                })
                .sorted(Comparator.comparing(ActiveUserStats::getBooksBorrowed).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    public OverviewStats getOverviewStats() {
        OverviewStats stats = new OverviewStats();

        List<Loan> allLoans = loanService.findAll();

        stats.setTotalBooks(bookService.count());
        stats.setTotalUsers(userService.count());
        stats.setBooksAvailable(bookService.findAll().stream().mapToLong(Book::getAvailableCopies).sum());
        stats.setBooksBorrowed(allLoans.stream().filter(l -> l.getStatus() == Loan.Status.ACTIVE).count());
        stats.setOverdueLoans(allLoans.stream()
                .filter(l -> l.getStatus() == Loan.Status.ACTIVE && l.getDueDate().isBefore(LocalDate.now().atStartOfDay()))
                .count());

        stats.setLoansToday(allLoans.stream()
                .filter(l -> l.getIssueDate().toLocalDate().equals(LocalDate.now()))
                .count());

        stats.setReturnsToday(allLoans.stream()
                .filter(l -> l.getReturnDate() != null && l.getReturnDate().toLocalDate().equals(LocalDate.now()))
                .count());

        return stats;
    }
}
