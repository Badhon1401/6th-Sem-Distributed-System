// ActiveUserStats.java
package com.bsse1401.stats_service.utils;

import lombok.Data;

@Data
public class ActiveUserStats {
    private Long userId;
    private String name;
    private Long booksBorrowed;
    private Long currentBorrows;
}
