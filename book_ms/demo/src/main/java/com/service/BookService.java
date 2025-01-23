package com.service;

import com.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface BookService {
    List<Book> findAll();
    Page<Book> findAll(Pageable pageable);
    Optional<Book> findById(Long id);
    Optional<Book> findByIsbn(String isbn);
    List<Book> findByCategory(String category);
    Page<Book> findByCategory(String category, Pageable pageable);
    List<Book> searchBooks(String keyword);
    Page<Book> searchBooks(String keyword, Pageable pageable);
    Book save(Book book);
    void deleteById(Long id);
    
    List<Book> getAllBooks();
    List<Book> getBooksByFilters(String category, String publisher);
    List<Book> getBooksByPublisher(String publisher);
    Book updateBook(String isbn, Book bookDetails);
    void deleteBook(String isbn);
    void deleteByIsbn(String isbn);
    Book addBook(Book book);
    Page<Book> findByPublisher(String publisher, Pageable pageable);
    Page<Book> findByFilters(String category, String publisher, Pageable pageable);
}


