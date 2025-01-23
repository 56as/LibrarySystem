package com.controller;

import com.model.Notification;
import com.model.Reader;
import com.model.User;
import com.service.NotificationService;
import com.service.ReaderService;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReaderService readerService;

    @GetMapping
    public String listNotifications(Model model) {
        // 获取当前登录用户
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        // 获取用户信息
        User user = userService.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        // 获取读者信息
        Reader reader = readerService.findByUser(user)
            .orElseThrow(() -> new RuntimeException("Reader not found"));

        // 检查是否有即将到期或已逾期的图书
        notificationService.checkAndCreateOverdueNotifications(reader.getId());

        // 获取通知列表
        List<Notification> notifications = notificationService.getReaderNotifications(reader.getId());
        
        model.addAttribute("notifications", notifications);
        return "notification/list";
    }

    @PostMapping("/{id}/read")
    @ResponseBody
    public void markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
    }

    // 管理员发送系统维护通知
    @PostMapping("/system")
    @ResponseBody
    public void sendSystemNotice(@RequestParam Long readerId,
                               @RequestParam String title,
                               @RequestParam String content) {
        Notification notification = new Notification();
        notification.setReader(readerService.findById(readerId)
                .orElseThrow(() -> new RuntimeException("Reader not found")));
        notification.setTitle(title);
        notification.setContent(content);
        notification.setType(Notification.NotificationType.ANNOUNCEMENT);
        notificationService.createNotification(notification);
    }
} 