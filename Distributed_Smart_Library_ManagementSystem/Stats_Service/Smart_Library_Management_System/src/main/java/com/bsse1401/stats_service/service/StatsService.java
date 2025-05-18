package com.bsse1401.stats_service.service;

import com.bsse1401.stats_service.model.Book;
import com.bsse1401.stats_service.model.Loan;
import com.bsse1401.stats_service.model.User;
import com.bsse1401.stats_service.utils.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final RestTemplate restTemplate;

    @Value("${user.service.url}")
    private String userServiceUrl;

    @Value("${book.service.url}")
    private String bookServiceUrl;

    @Value("${loan.service.url}")
    private String loanServiceUrl;

    public List<PopularBookStats> getPopularBooks() {
        ResponseEntity<List<Loan>> loanResponse = restTemplate.exchange(
                loanServiceUrl + "/api/loan_service/loans/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Loan>>() {}
        );
        List<Loan> loans = loanResponse.getBody();
        if (loans == null || loans.isEmpty()) return Collections.emptyList();

        ResponseEntity<List<Book>> bookResponse = restTemplate.exchange(
                bookServiceUrl + "/api/book_service/books/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Book>>() {}
        );
        List<Book> books = bookResponse.getBody();
        if (books == null || books.isEmpty()) return Collections.emptyList();

        Map<Long, Book> bookMap = books.stream()
                .collect(Collectors.toMap(Book::getBookId, b -> b));

        return loans.stream()
                .filter(loan -> bookMap.containsKey(loan.getBookId()))
                .collect(Collectors.groupingBy(Loan::getBookId, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .limit(10)
                .map(entry -> {
                    Book book = bookMap.get(entry.getKey());
                    PopularBookStats dto = new PopularBookStats();
                    dto.setBookId(book.getBookId());
                    dto.setTitle(book.getTitle());
                    dto.setAuthor(book.getAuthor());
                    dto.setBorrowCount(entry.getValue());
                    return dto;
                })
                .collect(Collectors.toList());
    }


    public List<ActiveUserStats> getActiveUsers() {
        ResponseEntity<List<Loan>> loanResponse = restTemplate.exchange(
                loanServiceUrl + "/api/loan_service/loans/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Loan>>() {}
        );
        List<Loan> loans = loanResponse.getBody();
        if (loans == null || loans.isEmpty()) return Collections.emptyList();

        ResponseEntity<List<User>> userResponse = restTemplate.exchange(
                userServiceUrl + "/api/user_service/users/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<User>>() {}
        );
        List<User> users = userResponse.getBody();
        if (users == null || users.isEmpty()) return Collections.emptyList();

        Map<Long, User> userMap = users.stream()
                .collect(Collectors.toMap(User::getId, u -> u));

        Map<Long, List<Loan>> userLoans = loans.stream()
                .filter(loan -> userMap.containsKey(loan.getUserId()))
                .collect(Collectors.groupingBy(Loan::getUserId));

        return userLoans.entrySet().stream()
                .map(entry -> {
                    User user = userMap.get(entry.getKey());
                    List<Loan> userLoanList = entry.getValue();
                    ActiveUserStats stats = new ActiveUserStats();
                    stats.setUserId(user.getId());
                    stats.setName(user.getName());
                    stats.setBooksBorrowed((long) userLoanList.size());
                    stats.setCurrentBorrows(userLoanList.stream()
                            .filter(l -> l.getStatus() == Loan.Status.ACTIVE)
                            .count());
                    return stats;
                })
                .sorted(Comparator.comparing(ActiveUserStats::getBooksBorrowed).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }


    public OverviewStats getOverviewStats() {
        OverviewStats stats = new OverviewStats();

        ResponseEntity<List<Loan>> response = restTemplate.exchange(
                loanServiceUrl + "/api/loan_service/loans/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Loan>>() {}
        );

        List<Loan> allLoans = response.getBody();
        if (allLoans == null) allLoans = Collections.emptyList();

        stats.setTotalBooks(restTemplate.getForObject(bookServiceUrl + "/api/book_service/books/count", Long.class));
        stats.setTotalUsers(restTemplate.getForObject(userServiceUrl + "/api/user_service/users/count", Long.class));

        List<Book> books = restTemplate.exchange(
                bookServiceUrl + "/api/book_service/books/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Book>>() {}
        ).getBody();

        if (books != null) {
            stats.setBooksAvailable(books.stream()
                    .filter(Objects::nonNull)
                    .mapToLong(Book::getAvailableCopies)
                    .sum());
        } else {
            stats.setBooksAvailable(0);
        }

        stats.setBooksBorrowed(allLoans.stream()
                .filter(l -> l.getStatus() == Loan.Status.ACTIVE)
                .count());

        stats.setOverdueLoans(allLoans.stream()
                .filter(l -> l.getStatus() == Loan.Status.ACTIVE
                        && l.getDueDate() != null
                        && l.getDueDate().isBefore(LocalDate.now().atStartOfDay()))
                .count());

        stats.setLoansToday(allLoans.stream()
                .filter(l -> l.getIssueDate() != null
                        && l.getIssueDate().toLocalDate().equals(LocalDate.now()))
                .count());

        stats.setReturnsToday(allLoans.stream()
                .filter(l -> l.getReturnDate() != null
                        && l.getReturnDate().toLocalDate().equals(LocalDate.now()))
                .count());

        return stats;
    }
}
