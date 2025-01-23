package com.controller;

import com.model.Admin;
import com.model.Announcement;
import com.service.AdminService;
import com.service.AnnouncementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/announcement_management")
@PreAuthorize("hasRole('ADMIN')")
public class AdminAnnouncementController {

    private static final Logger logger = LoggerFactory.getLogger(AdminAnnouncementController.class);

    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private AdminService adminService;

    @GetMapping("")
    public String list(Model model) {
        try {
            logger.info("开始获取公告列表");
            model.addAttribute("announcements", announcementService.findAll());
            logger.info("成功获取公告列表");
            return "announcement_management/list";
        } catch (Exception e) {
            logger.error("获取公告列表失败: {}", e.getMessage(), e);
            model.addAttribute("error", "获取公告列表失败：" + e.getMessage());
            return "announcement_management/list";
        }
    }

    @PostMapping("/publish")
    public String publish(@RequestParam String title,
                         @RequestParam String content,
                         RedirectAttributes redirectAttributes) {
        try {
            logger.info("开始发布公告，标题: {}", title);
            
            String encodedTitle = new String(title.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            String encodedContent = new String(content.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            logger.info("当前用户: {}", auth.getName());
            
            Admin admin = adminService.findByUsername(auth.getName());
            logger.info("找到管理员信息: {}", admin.getName());

            Announcement announcement = new Announcement();
            announcement.setTitle(encodedTitle);
            announcement.setContent(encodedContent);
            announcement.setAdmin(admin);

            announcementService.save(announcement);
            logger.info("公告发布成功");
            
            redirectAttributes.addFlashAttribute("success", "公告发布成功！");
        } catch (Exception e) {
            logger.error("发布公告失败: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "发布公告失败：" + e.getMessage());
        }
        return "redirect:/announcement_management";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            logger.info("开始删除公告，ID: {}", id);
            announcementService.deleteById(id);
            logger.info("公告删除成功");
            redirectAttributes.addFlashAttribute("success", "公告删除成功！");
        } catch (Exception e) {
            logger.error("删除公告失败: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "删除公告失败：" + e.getMessage());
        }
        return "redirect:/announcement_management";
    }
} 