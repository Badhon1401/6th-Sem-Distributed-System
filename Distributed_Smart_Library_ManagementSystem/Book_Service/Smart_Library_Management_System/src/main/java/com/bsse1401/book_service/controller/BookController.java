package com.bsse1401.book_service.controller;

import com.bsse1401.book_service.model.Book;
import com.bsse1401.book_service.service.BookService;
import com.bsse1401.book_service.utils.BookRequest;
import com.bsse1401.book_service.utils.BookUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book_service/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("/create")
    public ResponseEntity<Book> createBook(@Valid @RequestBody BookRequest request) {
        return new ResponseEntity<>(bookService.createBook(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Book>> searchBooks(@RequestParam(value = "search", required = false) String query) {
        if (query == null || query.isBlank()) {
            return ResponseEntity.ok(bookService.searchBooks(""));
        }
        return ResponseEntity.ok(bookService.searchBooks(query));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBook(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBook(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.findAll());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody BookUpdateRequest request) {
        return ResponseEntity.ok(bookService.updateBook(id, request));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/count")
    public ResponseEntity<Long> getAllBooksCount() {
        return ResponseEntity.ok(bookService.count());
    }

}
