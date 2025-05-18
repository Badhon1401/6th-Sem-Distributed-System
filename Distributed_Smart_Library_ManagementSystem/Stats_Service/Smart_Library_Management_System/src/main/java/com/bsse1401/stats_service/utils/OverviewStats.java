// OverviewStats.java
package com.bsse1401.stats_service.utils;

import lombok.Data;

@Data
public class OverviewStats {
    private long totalBooks;
    private long totalUsers;
    private long booksAvailable;
    private long booksBorrowed;
    private long overdueLoans;
    private long loansToday;
    private long returnsToday;
}

