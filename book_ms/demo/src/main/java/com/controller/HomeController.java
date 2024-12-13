package com.controller;

import com.model.Reader;
import com.model.User;
import com.service.BookService;
import com.service.BorrowingService;
import com.service.ReaderService;
import com.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private ReaderService readerService;

    @Autowired
    private BorrowingService borrowingService;

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        logger.debug("Accessing home page");
        
        // 添加页面标题
        model.addAttribute("pageTitle", "首页 - 图书管理系统");

        try {
            // 获取当前登录用户
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            logger.debug("Current authentication: {}", auth);

            // 如果用户未登录或是匿名用户，直接返回首页
            if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
                logger.debug("User is not authenticated or anonymous");
                return "home/index";
            }

            String username = auth.getName();
            logger.debug("Authenticated username: {}", username);

            // 获取用户信息
            User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
            logger.debug("User role: {}", user.getRole());

            model.addAttribute("username", username);
            model.addAttribute("role", user.getRole());

            // 根据角色添加不同的数据
            if (user.getRole() == User.UserRole.ADMIN || "ROLE_ADMIN".equals(user.getRole().toString())) {
                logger.debug("Loading admin dashboard data");
                model.addAttribute("isAdmin", true);
                model.addAttribute("totalBooks", bookService.findAll().size());
                model.addAttribute("totalReaders", readerService.findAll().size());
                model.addAttribute("totalBorrowings", borrowingService.findAll().size());
                model.addAttribute("activeBorrowings", borrowingService.findAllCurrentBorrowings().size());
            } else if (user.getRole() == User.UserRole.READER || "ROLE_READER".equals(user.getRole().toString())) {
                logger.debug("Loading reader dashboard data");
                model.addAttribute("isReader", true);
                Reader reader = readerService.findByUser(user)
                    .orElseThrow(() -> new RuntimeException("Reader not found for user: " + username));
                
                model.addAttribute("currentBorrowings", 
                    borrowingService.getReaderCurrentBorrowings(reader.getId()).size());
                model.addAttribute("borrowingHistory", 
                    borrowingService.getReaderBorrowingHistory(reader.getId()).size());
            }

            return "home/index";

        } catch (Exception e) {
            logger.error("Error loading home page: {}", e.getMessage(), e);
            model.addAttribute("error", "加载页面时发生错误：" + e.getMessage());
            return "home/index";
        }
    }
} 