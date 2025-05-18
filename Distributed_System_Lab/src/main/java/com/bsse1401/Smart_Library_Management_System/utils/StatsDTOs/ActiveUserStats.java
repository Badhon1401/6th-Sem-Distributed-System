// ActiveUserStats.java
package com.bsse1401.Smart_Library_Management_System.utils.StatsDTOs;

import lombok.Data;

@Data
public class ActiveUserStats {
    private Long userId;
    private String name;
    private Long booksBorrowed;
    private Long currentBorrows;
}
