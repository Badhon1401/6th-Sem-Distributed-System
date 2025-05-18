// PopularBookStats.java
package com.bsse1401.Smart_Library_Management_System.utils.StatsDTOs;

import lombok.Data;

@Data
public class PopularBookStats {
    private Long bookId;
    private String title;
    private String author;
    private Long borrowCount;
}
