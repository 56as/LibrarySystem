package com.repository;

import com.model.Borrowing;
import com.model.Borrowing.BorrowingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BorrowingRepository extends JpaRepository<Borrowing, Long> {
    List<Borrowing> findByReader_Id(Long readerId);
    List<Borrowing> findByReader_IdAndStatus(Long readerId, BorrowingStatus status);
    List<Borrowing> findByStatusAndDueDateBefore(BorrowingStatus status, LocalDate date);
    List<Borrowing> findByStatus(BorrowingStatus status);
    
    @Query(value = "SELECT r.name, COUNT(b.id) as borrowing_count " +
           "FROM readers r " +
           "JOIN borrowings b ON r.id = b.reader_id " +
           "GROUP BY r.id, r.name " +
           "ORDER BY borrowing_count DESC " +
           "LIMIT ?1", nativeQuery = true)
    List<Object[]> findMostActiveReaders(int limit);

    @Query(value = "SELECT DATE_FORMAT(borrow_date, '%Y-%m') as month, COUNT(*) as count " +
           "FROM borrowings " +
           "GROUP BY month " +
           "ORDER BY month DESC", nativeQuery = true)
    List<Object[]> findMonthlyBorrowingStats();
}
