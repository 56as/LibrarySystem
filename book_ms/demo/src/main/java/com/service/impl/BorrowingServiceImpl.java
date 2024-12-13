package com.service.impl;

import com.model.Book;
import com.model.Borrowing;
import com.model.Borrowing.BorrowingStatus;
import com.model.Reader;
import com.repository.BookRepository;
import com.repository.BorrowingRepository;
import com.repository.ReaderRepository;
import com.service.BorrowingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BorrowingServiceImpl implements BorrowingService {

    private static final Logger logger = LoggerFactory.getLogger(BorrowingServiceImpl.class);

    @Autowired
    private BorrowingRepository borrowingRepository;

    @Autowired
    private ReaderRepository readerRepository;

    @Autowired
    private BookRepository bookRepository;

    private static final int DEFAULT_BORROWING_DAYS = 30;
    private static final int MAX_RENEWALS = 2;

    @Override
    public List<Borrowing> findAll() {
        updateOverdueBorrowings(); // 在获取列表时更新状态
        return borrowingRepository.findAll();
    }

    @Override
    public Page<Borrowing> findAll(Pageable pageable) {
        updateOverdueBorrowings(); // 在获取列表时更新状态
        return borrowingRepository.findAll(pageable);
    }

    // 自动更新逾期状态
    @Transactional
    public void updateOverdueBorrowings() {
        logger.debug("Updating overdue borrowings status");
        LocalDate today = LocalDate.now();
        List<Borrowing> borrowings = borrowingRepository.findByStatusAndDueDateBefore(
            BorrowingStatus.BORROWED, today);
        
        for (Borrowing borrowing : borrowings) {
            borrowing.setStatus(BorrowingStatus.OVERDUE);
            borrowingRepository.save(borrowing);
            logger.debug("Updated borrowing {} to OVERDUE status", borrowing.getId());
        }
    }

    // 每天凌晨自动检查并更新逾期状态
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void scheduledOverdueCheck() {
        updateOverdueBorrowings();
    }

    @Override
    public Optional<Borrowing> findById(Long id) {
        return borrowingRepository.findById(id);
    }

    @Override
    public Borrowing save(Borrowing borrowing) {
        return borrowingRepository.save(borrowing);
    }

    @Override
    public void deleteById(Long id) {
        borrowingRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Borrowing borrowBook(String readerId, String bookIsbn) {
        logger.debug("Attempting to borrow book: bookIsbn={}, readerId={}", bookIsbn, readerId);
        
        try {
            Book book = bookRepository.findById(bookIsbn)
                    .orElseThrow(() -> new IllegalArgumentException("图书不存在: " + bookIsbn));
            
            Reader reader = readerRepository.findById(Long.parseLong(readerId))
                    .orElseThrow(() -> new IllegalArgumentException("读者不存在: " + readerId));

            if (book.getAvailableCopies() <= 0) {
                throw new IllegalStateException("图书已无可借数量");
            }

            // 检查读者是否已借阅此书
            List<Borrowing> currentBorrowings = borrowingRepository.findByReader_IdAndStatus(
                Long.parseLong(readerId), BorrowingStatus.BORROWED);
            
            for (Borrowing existing : currentBorrowings) {
                if (existing.getBook().getIsbn().equals(bookIsbn)) {
                    throw new IllegalStateException("您已借阅此书，尚未归还");
                }
            }

            Borrowing borrowing = new Borrowing();
            borrowing.setReader(reader);
            borrowing.setBook(book);
            borrowing.setBorrowDate(LocalDate.now());
            borrowing.setDueDate(LocalDate.now().plusDays(DEFAULT_BORROWING_DAYS));
            borrowing.setStatus(BorrowingStatus.BORROWED);
            borrowing.setRenewalCount(0);

            // 更新图书可借数量
            book.setAvailableCopies(book.getAvailableCopies() - 1);
            bookRepository.save(book);

            logger.debug("Successfully created borrowing record");
            return borrowingRepository.save(borrowing);
            
        } catch (Exception e) {
            logger.error("Error while borrowing book: ", e);
            throw e;
        }
    }

    @Override
    @Transactional
    public Borrowing returnBook(Long borrowingId) {
        Borrowing borrowing = borrowingRepository.findById(borrowingId)
                .orElseThrow(() -> new IllegalArgumentException("借阅记录不存在"));

        if (borrowing.getStatus() == BorrowingStatus.RETURNED) {
            throw new IllegalStateException("图书已归还");
        }

        borrowing.setReturnDate(LocalDate.now());
        borrowing.setStatus(BorrowingStatus.RETURNED);

        Book book = borrowing.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        return borrowingRepository.save(borrowing);
    }

    @Override
    @Transactional
    public Borrowing renewBook(Long borrowingId) {
        Borrowing borrowing = borrowingRepository.findById(borrowingId)
                .orElseThrow(() -> new IllegalArgumentException("借阅记录不存在"));

        if (borrowing.getStatus() == BorrowingStatus.RETURNED) {
            throw new IllegalStateException("已归还的图书不能续借");
        }

        if (borrowing.getRenewalCount() >= MAX_RENEWALS) {
            throw new IllegalStateException("已达到最大续借次数");
        }

        borrowing.setDueDate(borrowing.getDueDate().plusDays(DEFAULT_BORROWING_DAYS));
        borrowing.setRenewalCount(borrowing.getRenewalCount() + 1);
        borrowing.setStatus(BorrowingStatus.BORROWED); // 续借后重置状态为借出

        return borrowingRepository.save(borrowing);
    }

    @Override
    public List<Borrowing> getReaderBorrowingHistory(Long readerId) {
        updateOverdueBorrowings(); // 在获取列表时更新状态
        return borrowingRepository.findByReader_Id(readerId);
    }

    @Override
    public List<Borrowing> getReaderCurrentBorrowings(Long readerId) {
        updateOverdueBorrowings(); // 在获取列表时更新状态
        return borrowingRepository.findByReader_IdAndStatus(readerId, BorrowingStatus.BORROWED);
    }

    @Override
    public List<Borrowing> getOverdueBorrowings() {
        updateOverdueBorrowings(); // 在获取列表时更新状态
        return borrowingRepository.findByStatusAndDueDateBefore(BorrowingStatus.BORROWED, LocalDate.now());
    }

    @Override
    public List<Object[]> getMostActiveReaders(int limit) {
        return borrowingRepository.findMostActiveReaders(limit);
    }

    @Override
    public List<Object[]> getMonthlyBorrowingStats() {
        return borrowingRepository.findMonthlyBorrowingStats();
    }

    @Override
    public List<Borrowing> findAllCurrentBorrowings() {
        updateOverdueBorrowings(); // 在获取列表时更新状态
        return borrowingRepository.findByStatus(BorrowingStatus.BORROWED);
    }
} 