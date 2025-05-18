package com.bsse1401.stats_service.controller;

import com.bsse1401.stats_service.service.StatsService;
import com.bsse1401.stats_service.utils.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/stats_service/stats")
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
