package com.bsse1401.book_service.service;

import com.bsse1401.book_service.model.Book;
import com.bsse1401.book_service.repository.BookRepository;
import com.bsse1401.book_service.utils.BookUpdateRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepo;

    public Book createBook(com.bsse1401.book_service.utils.@Valid BookRequest request) {
        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setCopies(request.getCopies());
        book.setAvailableCopies(request.getCopies()); // all are available initially
        return bookRepo.save(book);
    }

    public Book getBook(Long id) {
        return bookRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ID: " + id));
    }

    public List<Book> searchBooks(String query) {
        return bookRepo.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrIsbnContainingIgnoreCase(query, query, query);
    }

    public Book updateBook(Long id, BookUpdateRequest request) {
        Book book = getBook(id);
        if (request.getCopies() != null) {
            if (request.getAvailableCopies() != null && request.getAvailableCopies() > request.getCopies()) {
                throw new IllegalArgumentException("Available copies can't exceed total copies");
            }
            book.setCopies(request.getCopies());
        }
        if (request.getAvailableCopies() != null) {
            book.setAvailableCopies(request.getAvailableCopies());
        }
        return bookRepo.save(book);
    }

    public void deleteBook(Long id) {
        if (!bookRepo.existsById(id)) {
            throw new EntityNotFoundException("Book not found with ID: " + id);
        }
        bookRepo.deleteById(id);
    }

    public List<Book> findAll() {
        return bookRepo.findAll();
    }

    public long count() {
        return bookRepo.count();
    }

}
