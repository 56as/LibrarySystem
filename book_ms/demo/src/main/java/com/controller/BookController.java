package com.controller;

import com.model.Book;
import com.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/books")
public class BookController {
    private static final Logger logger = LoggerFactory.getLogger(BookController.class);
    private static final int PAGE_SIZE = 10;

    @Autowired
    private BookService bookService;

    @GetMapping
    public String listBooks(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "1") int page,
            Model model) {
        try {
            logger.info("Attempting to list books with parameters - keyword: {}, category: {}, page: {}", 
                       keyword, category, page);
            
            Page<Book> bookPage;
            if (keyword != null && !keyword.trim().isEmpty()) {
                logger.debug("Searching books with keyword: {}", keyword);
                bookPage = bookService.searchBooks(keyword.trim(), PageRequest.of(page - 1, PAGE_SIZE));
            } else if (category != null && !category.trim().isEmpty()) {
                logger.debug("Filtering books by category: {}", category);
                bookPage = bookService.findByCategory(category.trim(), PageRequest.of(page - 1, PAGE_SIZE));
            } else {
                logger.debug("Fetching all books for page: {}", page);
                bookPage = bookService.findAll(PageRequest.of(page - 1, PAGE_SIZE));
            }
            
            logger.debug("Retrieved {} books", bookPage.getContent().size());
            
            model.addAttribute("books", bookPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", bookPage.getTotalPages());
            model.addAttribute("totalItems", bookPage.getTotalElements());
            model.addAttribute("keyword", keyword);
            model.addAttribute("category", category);
            
            if (bookPage.isEmpty()) {
                logger.info("No books found for the given criteria");
                model.addAttribute("info", "未找到符合条件的图书");
            }
            
        } catch (Exception e) {
            logger.error("Error retrieving books: ", e);
            logger.error("Exception details - Message: {}, Cause: {}", e.getMessage(), e.getCause());
            logger.error("Stack trace: ", e);
            model.addAttribute("error", "获取图书列表失败: " + e.getMessage());
        }
        
        model.addAttribute("pageTitle", "图书列表 - 图书管理系统");
        return "book/list";
    }
}
