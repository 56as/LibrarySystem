package com.controller;

import com.model.Borrowing;
import com.model.Reader;
import com.model.User;
import com.service.BorrowingService;
import com.service.ReaderService;
import com.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/borrowings")
public class BorrowingController {
    
    private static final Logger logger = LoggerFactory.getLogger(BorrowingController.class);

    @Autowired
    private BorrowingService borrowingService;

    @Autowired
    private ReaderService readerService;

    @Autowired
    private UserService userService;

    // 显示借阅列表页面
    @GetMapping
    public String listBorrowings(
            @RequestParam(defaultValue = "1") int page,
            Model model) {
        
        try {
            // 在获取借阅列表前更新逾期状态
            borrowingService.updateOverdueBorrowings();
            
            // 获取分页数据
            Page<Borrowing> borrowingPage = borrowingService.findAll(PageRequest.of(page - 1, 10));
            
            model.addAttribute("borrowings", borrowingPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", borrowingPage.getTotalPages());
            model.addAttribute("pageTitle", "借阅管理");
            
            logger.debug("Loaded {} borrowings for page {}", 
                borrowingPage.getContent().size(), page);
            
        } catch (Exception e) {
            logger.error("Error loading borrowings list: ", e);
            model.addAttribute("error", "加载借阅记录失败: " + e.getMessage());
        }
        
        return "borrowing/list";
    }

    // 处理借阅请求
    @PostMapping("/borrow")
    @ResponseBody
    public ResponseEntity<?> borrowBook(@RequestBody Map<String, String> request) {
        try {
            // 获取当前登录用户
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            
            // 获取用户信息
            User user = userService.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在: " + username));
            
            // 获取读者信息
            Reader reader = readerService.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("读者信息不存在: " + username));

            // 获取图书ISBN并创建借阅
            String isbn = request.get("isbn");
            if (isbn == null || isbn.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("图书ISBN不能为空");
            }

            // 创建借阅
            Borrowing borrowing = borrowingService.borrowBook(String.valueOf(reader.getId()), isbn);
            return ResponseEntity.ok(borrowing);

        } catch (IllegalArgumentException e) {
            logger.error("借阅参数错误: ", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            logger.error("借阅状态错误: ", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("借阅失败: ", e);
            return ResponseEntity.internalServerError().body("借阅失败：" + e.getMessage());
        }
    }

    // 显示借阅详情页面
    @GetMapping("/{id}/detail")
    public String showBorrowingDetail(@PathVariable Long id, Model model) {
        borrowingService.findById(id).ifPresent(borrowing -> {
            model.addAttribute("borrowing", borrowing);
            model.addAttribute("pageTitle", "借阅详情");
        });
        return "borrowing/detail";
    }

    // 获取读者的借阅历史
    @GetMapping("/api/reader/{readerId}/history")
    @ResponseBody
    public ResponseEntity<List<Borrowing>> getReaderBorrowingHistory(@PathVariable Long readerId) {
        return ResponseEntity.ok(borrowingService.getReaderBorrowingHistory(readerId));
    }

    // 获取读者的当前借阅
    @GetMapping("/api/reader/{readerId}/current")
    @ResponseBody
    public ResponseEntity<List<Borrowing>> getReaderCurrentBorrowings(@PathVariable Long readerId) {
        return ResponseEntity.ok(borrowingService.getReaderCurrentBorrowings(readerId));
    }

    // 获取所有逾期借阅
    @GetMapping("/api/overdue")
    @ResponseBody
    public ResponseEntity<List<Borrowing>> getOverdueBorrowings() {
        return ResponseEntity.ok(borrowingService.getOverdueBorrowings());
    }
} 