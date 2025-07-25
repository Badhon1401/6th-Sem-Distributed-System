// PopularBookStats.java
package com.bsse1401.stats_service.utils;

import lombok.Data;

@Data
public class PopularBookStats {
    private Long bookId;
    private String title;
    private String author;
    private Long borrowCount;
}
