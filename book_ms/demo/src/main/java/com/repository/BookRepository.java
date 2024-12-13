package com.repository;

import com.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, String> {
    
    @Query("SELECT b FROM Book b WHERE " +
           "LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.author) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.isbn) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Book> searchBooks(@Param("keyword") String keyword);

    @Query("SELECT b FROM Book b WHERE " +
           "LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.author) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.isbn) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Book> searchBooks(@Param("keyword") String keyword, Pageable pageable);

    List<Book> findByCategory(String category);
    Page<Book> findByCategory(String category, Pageable pageable);
    
    List<Book> findByPublisher(String publisher);
    Page<Book> findByPublisher(String publisher, Pageable pageable);
    
    @Query("SELECT b FROM Book b WHERE " +
           "(:category IS NULL OR b.category = :category) AND " +
           "(:publisher IS NULL OR b.publisher = :publisher)")
    List<Book> findByFilters(@Param("category") String category, 
                            @Param("publisher") String publisher);

    @Query("SELECT b FROM Book b WHERE " +
           "(:category IS NULL OR b.category = :category) AND " +
           "(:publisher IS NULL OR b.publisher = :publisher)")
    Page<Book> findByFilters(@Param("category") String category, 
                            @Param("publisher") String publisher,
                            Pageable pageable);
}
