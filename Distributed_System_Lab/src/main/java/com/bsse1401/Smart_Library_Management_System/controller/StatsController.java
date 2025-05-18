package com.bsse1401.Smart_Library_Management_System.controller;

import com.bsse1401.Smart_Library_Management_System.service.StatsService;
import com.bsse1401.Smart_Library_Management_System.utils.StatsDTOs.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @GetMapping("/books/popular")
    public ResponseEntity<List<PopularBookStats>> getPopularBooks() {
        return ResponseEntity.ok(statsService.getPopularBooks());
    }

    @GetMapping("/users/active")
    public ResponseEntity<List<ActiveUserStats>> getActiveUsers() {
        return ResponseEntity.ok(statsService.getActiveUsers());
    }

    @GetMapping("/overview")
    public ResponseEntity<OverviewStats> getOverview() {
        return ResponseEntity.ok(statsService.getOverviewStats());
    }
}
