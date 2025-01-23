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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping(value = "/books_management", produces = "text/html;charset=UTF-8")
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
            model.addAttribute("category", category);
            model.addAttribute("publisher", publisher);

            return "books_management/list";
        } catch (Exception e) {
            logger.error("Error showing books page: ", e);
            model.addAttribute("error", "获取图书列表失败: " + e.getMessage());
            return "books_management/list";
        }
    }

    @PostMapping("/{isbn}/delete")
    public String deleteBook(@PathVariable String isbn, RedirectAttributes redirectAttributes) {
        try {
            bookService.deleteByIsbn(isbn);
            redirectAttributes.addFlashAttribute("success", "图书删除成功");
        } catch (Exception e) {
            logger.error("Error deleting book: ", e);
            redirectAttributes.addFlashAttribute("error", "删除图书失败: " + e.getMessage());
        }
        return "redirect:/books_management";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("book", new Book());
        return "books_management/add";
    }

    @PostMapping(value = "/add", produces = "text/html;charset=UTF-8")
    public String addBook(@ModelAttribute Book book, RedirectAttributes redirectAttributes) {
        try {
            // 确保所有字符串字段使用UTF-8编码
            if (book.getTitle() != null) {
                book.setTitle(new String(book.getTitle().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
            }
            if (book.getAuthor() != null) {
                book.setAuthor(new String(book.getAuthor().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
            }
            if (book.getPublisher() != null) {
                book.setPublisher(new String(book.getPublisher().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
            }
            if (book.getCategory() != null) {
                book.setCategory(new String(book.getCategory().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
            }

            if (bookService.findByIsbn(book.getIsbn()).isPresent()) {
                redirectAttributes.addFlashAttribute("error", "ISBN已存在");
                return "redirect:/books_management/add";
            }

            // 设置可借数量等于总数量
            book.setAvailableCopies(book.getTotalCopies());
            
            // 如果出版日期为空，设置为当前日期
            if (book.getPublishDate() == null) {
                book.setPublishDate(LocalDate.now());
            }

            bookService.addBook(book);
            redirectAttributes.addFlashAttribute("success", "添加图书成功");
            return "redirect:/books_management";
        } catch (Exception e) {
            logger.error("Error adding book: ", e);
            redirectAttributes.addFlashAttribute("error", "添加图书失败: " + e.getMessage());
            return "redirect:/books_management/add";
        }
    }
} 