package com.controller;

import com.model.Book;
import com.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/books")
public class AdminBookController {
    private static final Logger logger = LoggerFactory.getLogger(AdminBookController.class);
    private static final int PAGE_SIZE = 10;

    @Autowired
    private BookService bookService;

    @GetMapping
    public String showBooks(Model model,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String publisher,
            @RequestParam(defaultValue = "1") int page) {
        try {
            logger.info("Showing books page {} with filters - keyword: {}, category: {}, publisher: {}", 
                       page, keyword, category, publisher);
            
            PageRequest pageRequest = PageRequest.of(Math.max(0, page - 1), PAGE_SIZE);
            Page<Book> bookPage;

            if (keyword != null && !keyword.trim().isEmpty()) {
                bookPage = bookService.searchBooks(keyword.trim(), pageRequest);
            } else if ((category != null && !category.trim().isEmpty()) || 
                      (publisher != null && !publisher.trim().isEmpty())) {
                String categoryValue = category != null && !category.trim().isEmpty() ? category.trim() : null;
                String publisherValue = publisher != null && !publisher.trim().isEmpty() ? publisher.trim() : null;
                bookPage = bookService.findByFilters(categoryValue, publisherValue, pageRequest);
            } else {
                bookPage = bookService.findAll(pageRequest);
            }

            model.addAttribute("books", bookPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", bookPage.getTotalPages());
            model.addAttribute("totalItems", bookPage.getTotalElements());
            model.addAttribute("keyword", keyword);
            model.addAttribute("selectedCategory", category);
            model.addAttribute("selectedPublisher", publisher);

            if (bookPage.isEmpty()) {
                model.addAttribute("info", "未找到符合条件���图书");
            }

            return "admin/books";
        } catch (Exception e) {
            logger.error("Error showing books page: ", e);
            model.addAttribute("error", "获取图书列表失败: " + e.getMessage());
            return "admin/books";
        }
    }

    @GetMapping("/api/{isbn}")
    @ResponseBody
    public ResponseEntity<?> getBook(@PathVariable String isbn) {
        try {
            logger.info("Getting book details for ISBN: {}", isbn);
            return bookService.findByIsbn(isbn)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> {
                        logger.warn("Book not found with ISBN: {}", isbn);
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception e) {
            logger.error("Error getting book details for ISBN {}: ", isbn, e);
            return ResponseEntity.badRequest().body("获取图书详情失败: " + e.getMessage());
        }
    }

    @PostMapping("/api")
    @ResponseBody
    @Transactional
    public ResponseEntity<?> addBook(@RequestBody Book book) {
        try {
            logger.info("Adding new book with ISBN: {}", book.getIsbn());
            
            if (bookService.findByIsbn(book.getIsbn()).isPresent()) {
                logger.warn("Book with ISBN {} already exists", book.getIsbn());
                return ResponseEntity.badRequest().body("ISBN已存在");
            }

            book.setAvailableCopies(book.getTotalCopies());
            Book savedBook = bookService.addBook(book);
            logger.info("Successfully added book with ISBN: {}", savedBook.getIsbn());
            return ResponseEntity.ok(savedBook);
        } catch (Exception e) {
            logger.error("Error adding book: ", e);
            return ResponseEntity.badRequest().body("添加图书失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/api/{isbn}")
    @ResponseBody
    public ResponseEntity<?> deleteBook(@PathVariable String isbn) {
        try {
            bookService.deleteBook(isbn);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error deleting book: ", e);
            return ResponseEntity.badRequest().body("删除图书失败: " + e.getMessage());
        }
    }

    @PutMapping("/api/{isbn}")
    @ResponseBody
    public ResponseEntity<?> updateBook(@PathVariable String isbn, @RequestBody Book book) {
        try {
            if (!isbn.equals(book.getIsbn())) {
                return ResponseEntity.badRequest().body("ISBN不匹配");
            }
            Book updatedBook = bookService.updateBook(isbn, book);
            return ResponseEntity.ok(updatedBook);
        } catch (Exception e) {
            logger.error("Error updating book: ", e);
            return ResponseEntity.badRequest().body("更新图书失败: " + e.getMessage());
        }
    }
} 