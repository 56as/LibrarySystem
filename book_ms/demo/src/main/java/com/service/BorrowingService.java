package com.service;

import com.model.Borrowing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface BorrowingService {
    List<Borrowing> findAll();
    Page<Borrowing> findAll(Pageable pageable);
    Optional<Borrowing> findById(Long id);
    Borrowing save(Borrowing borrowing);
    void deleteById(Long id);
    Borrowing borrowBook(String readerId, String bookId);
    Borrowing returnBook(Long borrowingId);
    Borrowing renewBook(Long borrowingId);
    List<Borrowing> getReaderBorrowingHistory(Long readerId);
    List<Borrowing> getReaderCurrentBorrowings(Long readerId);
    List<Borrowing> getOverdueBorrowings();
    List<Object[]> getMostActiveReaders(int limit);
    List<Object[]> getMonthlyBorrowingStats();
    List<Borrowing> findAllCurrentBorrowings();
    void updateOverdueBorrowings();
}

