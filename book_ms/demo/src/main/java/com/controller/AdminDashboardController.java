package com.controller;

import com.service.BookService;
import com.service.ReaderService;
import com.service.BorrowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    @Autowired
    private BookService bookService;

    @Autowired
    private ReaderService readerService;

    @Autowired
    private BorrowingService borrowingService;

    @GetMapping
    public String dashboard(Model model) {
        // 添加统计数据
        try {
            model.addAttribute("bookCount", bookService.findAll().size());
        } catch (Exception e) {
            model.addAttribute("bookCount", 0);
        }

        try {
            model.addAttribute("readerCount", readerService.findAll().size());
        } catch (Exception e) {
            model.addAttribute("readerCount", 0);
        }

        try {
            model.addAttribute("borrowingCount", borrowingService.findAll().size());
        } catch (Exception e) {
            model.addAttribute("borrowingCount", 0);
        }

        model.addAttribute("pageTitle", "管理控制台 - 图书管理系统");
        return "admin/dashboard";
    }
} 