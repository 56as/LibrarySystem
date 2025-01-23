package com.controller;

import com.model.Reader;
import com.model.User;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/profile", produces = "text/html;charset=UTF-8")
public class ProfileController {
    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ReaderService readerService;

    @Autowired
    private BorrowingService borrowingService;

    @GetMapping("/borrowings")
    public String showBorrowings(Model model) {
        try {
            // 获取当前登录用户
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            
            // 获取用户信息
            User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
            
            // 获取读者信息
            Reader reader = readerService.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Reader not found for user: " + username));
            
            // 获取借阅信息
            model.addAttribute("currentBorrowings", 
                borrowingService.getReaderCurrentBorrowings(reader.getId()));
            model.addAttribute("borrowingHistory", 
                borrowingService.getReaderBorrowingHistory(reader.getId()));
            
            model.addAttribute("pageTitle", "我的借阅 - 图书管理系统");
            return "profile/borrowings";
            
        } catch (Exception e) {
            logger.error("Error loading borrowings page", e);
            model.addAttribute("error", "加载借阅信息失败: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/info")
    public String showInfo(Model model) {
        try {
            // 获取当前登录用户
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            
            // 获取用户信息
            User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
            
            // 获取读者信息
            Reader reader = readerService.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Reader not found for user: " + username));
            
            // 添加到模型
            model.addAttribute("user", user);
            model.addAttribute("reader", reader);
            
            model.addAttribute("pageTitle", "个人信息 - 图书管理系统");
            return "profile/info";
            
        } catch (Exception e) {
            logger.error("Error loading profile page", e);
            model.addAttribute("error", "加载个人信息失败: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/edit")
    public String showEditForm(Model model) {
        try {
            // 获取当前登录用户
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            
            // 获取用户信息
            User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
            
            // 获取读者信息
            Reader reader = readerService.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Reader not found for user: " + username));
            
            // 添加到模型
            model.addAttribute("user", user);
            model.addAttribute("reader", reader);
            
            return "profile/edit";
            
        } catch (Exception e) {
            logger.error("Error loading edit profile page", e);
            model.addAttribute("error", "加载编辑页面失败: " + e.getMessage());
            return "error";
        }
    }

    @PostMapping("/update")
    public String updateProfile(
            @RequestParam("name") String name,
            @RequestParam("phone") String phone,
            @RequestParam("email") String email,
            @RequestParam("address") String address,
            RedirectAttributes redirectAttributes) {
        try {
            // 获取当前登录用户
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            
            // 获取用户信息
            User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
            
            // 获取读者信息
            Reader reader = readerService.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Reader not found for user: " + username));
            
            // 更新用户信息
            user.setEmail(email);
            userService.save(user);
            
            // 更新读者信息
            reader.setName(name);
            reader.setPhone(phone);
            reader.setAddress(address);
            readerService.save(reader);
            
            redirectAttributes.addFlashAttribute("success", "个人资料更新成功！");
            return "redirect:/profile/info";
            
        } catch (Exception e) {
            logger.error("Error updating profile", e);
            redirectAttributes.addFlashAttribute("error", "更新个人资料失败: " + e.getMessage());
            return "redirect:/profile/edit";
        }
    }

    @GetMapping("/return")
    public String showReturnPage(Model model) {
        try {
            // 获取当前登录用户
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            
            // 获取用户信息
            User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
            
            // 获取读者信息
            Reader reader = readerService.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Reader not found for user: " + username));
            
            // 获取当前借阅列表
            model.addAttribute("currentBorrowings", 
                borrowingService.getReaderCurrentBorrowings(reader.getId()));
            
            return "profile/return";
            
        } catch (Exception e) {
            logger.error("Error loading return page", e);
            model.addAttribute("error", "加载归还页面失败: " + e.getMessage());
            return "error";
        }
    }

    @PostMapping("/return-book/{id}")
    public String returnBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            borrowingService.returnBook(id);
            redirectAttributes.addFlashAttribute("success", "图书归还成功！");
        } catch (Exception e) {
            logger.error("Error returning book", e);
            redirectAttributes.addFlashAttribute("error", "归还失败: " + e.getMessage());
        }
        return "redirect:/profile/return";
    }

    // 默认路由重定向到个人信息页面
    @GetMapping
    public String redirectToInfo() {
        return "redirect:/profile/info";
    }
} 