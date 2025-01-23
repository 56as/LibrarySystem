package com.service.impl;

import com.model.Book;
import com.repository.BookRepository;
import com.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookServiceImpl implements BookService {
    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);

    @Autowired
    private BookRepository bookRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Book> findAll() {
        try {
            return bookRepository.findAll();
        } catch (Exception e) {
            logger.error("Error finding all books: ", e);
            throw new RuntimeException("获取图书列表失败", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Book> findAll(Pageable pageable) {
        try {
            return bookRepository.findAll(pageable);
        } catch (Exception e) {
            logger.error("Error finding books with pagination: ", e);
            throw new RuntimeException("获取图书列表失败", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> findById(Long id) {
        try {
            return bookRepository.findById(id.toString());
        } catch (Exception e) {
            logger.error("Error finding book by id {}: ", id, e);
            throw new RuntimeException("获取图书信息失败", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> findByIsbn(String isbn) {
        try {
            return bookRepository.findById(isbn);
        } catch (Exception e) {
            logger.error("Error finding book by ISBN {}: ", isbn, e);
            throw new RuntimeException("获取图书信息失败", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> findByCategory(String category) {
        try {
            return bookRepository.findByCategory(category);
        } catch (Exception e) {
            logger.error("Error finding books by category {}: ", category, e);
            throw new RuntimeException("获取图书列表失败", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Book> findByCategory(String category, Pageable pageable) {
        try {
            return bookRepository.findByCategory(category, pageable);
        } catch (Exception e) {
            logger.error("Error finding books by category {} with pagination: ", category, e);
            throw new RuntimeException("获取图书列表失败", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> searchBooks(String keyword) {
        try {
            return bookRepository.searchBooks(keyword);
        } catch (Exception e) {
            logger.error("Error searching books with keyword {}: ", keyword, e);
            throw new RuntimeException("搜索图书失败", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Book> searchBooks(String keyword, Pageable pageable) {
        try {
            return bookRepository.searchBooks(keyword, pageable);
        } catch (Exception e) {
            logger.error("Error searching books with keyword {} and pagination: ", keyword, e);
            throw new RuntimeException("搜索图书失败", e);
        }
    }

    @Override
    @Transactional
    public Book save(Book book) {
        try {
            return bookRepository.save(book);
        } catch (Exception e) {
            logger.error("Error saving book: ", e);
            throw new RuntimeException("保存图书失败", e);
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        try {
            bookRepository.deleteById(id.toString());
        } catch (Exception e) {
            logger.error("Error deleting book by id {}: ", id, e);
            throw new RuntimeException("删除图书失败", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        try {
            return bookRepository.findAll();
        } catch (Exception e) {
            logger.error("Error getting all books: ", e);
            throw new RuntimeException("获取图书列表失败", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getBooksByFilters(String category, String publisher) {
        try {
            return bookRepository.findByFilters(category, publisher);
        } catch (Exception e) {
            logger.error("Error getting books by filters - category: {}, publisher: {}: ", category, publisher, e);
            throw new RuntimeException("获取图书列表失败", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getBooksByPublisher(String publisher) {
        try {
            return bookRepository.findByPublisher(publisher);
        } catch (Exception e) {
            logger.error("Error getting books by publisher {}: ", publisher, e);
            throw new RuntimeException("获取图书列表失败", e);
        }
    }

    @Override
    @Transactional
    public Book updateBook(String isbn, Book bookDetails) {
        try {
            Book book = bookRepository.findById(isbn)
                .orElseThrow(() -> new RuntimeException("图书不存在"));

            book.setTitle(bookDetails.getTitle());
            book.setAuthor(bookDetails.getAuthor());
            book.setPublisher(bookDetails.getPublisher());
            book.setCategory(bookDetails.getCategory());
            book.setTotalCopies(bookDetails.getTotalCopies());

            // 保持可借数量与总数量的差值不变
            int unavailableCopies = book.getTotalCopies() - book.getAvailableCopies();
            book.setAvailableCopies(bookDetails.getTotalCopies() - unavailableCopies);

            return bookRepository.save(book);
        } catch (Exception e) {
            logger.error("Error updating book with ISBN {}: ", isbn, e);
            throw new RuntimeException("更新图书失败", e);
        }
    }

    @Override
    @Transactional
    public void deleteBook(String isbn) {
        try {
            logger.info("Attempting to delete book with ISBN: {}", isbn);
            
            // Check if book exists before deletion
            Optional<Book> book = bookRepository.findById(isbn);
            if (!book.isPresent()) {
                logger.warn("Book with ISBN {} not found", isbn);
                throw new RuntimeException("图书不存在");
            }
            
            bookRepository.deleteById(isbn);
            logger.info("Successfully deleted book with ISBN: {}", isbn);
        } catch (Exception e) {
            logger.error("Error deleting book with ISBN {}: ", isbn, e);
            throw new RuntimeException("删除图书失败: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public Book addBook(Book book) {
        try {
            book.setAvailableCopies(book.getTotalCopies());
            return bookRepository.save(book);
        } catch (Exception e) {
            logger.error("Error adding book: ", e);
            throw new RuntimeException("添加图书失败", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Book> findByPublisher(String publisher, Pageable pageable) {
        try {
            return bookRepository.findByPublisher(publisher, pageable);
        } catch (Exception e) {
            logger.error("Error finding books by publisher {} with pagination: ", publisher, e);
            throw new RuntimeException("获取图书列表失败", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Book> findByFilters(String category, String publisher, Pageable pageable) {
        try {
            return bookRepository.findByFilters(category, publisher, pageable);
        } catch (Exception e) {
            logger.error("Error finding books with filters - category: {}, publisher: {}: ", category, publisher, e);
            throw new RuntimeException("获取图书列表失败", e);
        }
    }

    @Override
    @Transactional
    public void deleteByIsbn(String isbn) {
        deleteBook(isbn);
    }
} 