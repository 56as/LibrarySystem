package com.controller;

import com.model.Reader;
import com.model.User;
import com.service.ReaderService;
import com.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/admin/readers", produces = "text/html;charset=UTF-8")
public class AdminReaderController {
    
    private static final Logger logger = LoggerFactory.getLogger(AdminReaderController.class);
    
    @Autowired
    private ReaderService readerService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/new")
    public String showAddReaderForm(Model model) {
        return "admin/reader/form";
    }
    
    @PostMapping("/new")
    public String addReader(@RequestParam String name,
                          @RequestParam String username,
                          @RequestParam String email,
                          @RequestParam String password,
                          @RequestParam(required = false) String phone,
                          @RequestParam(required = false) String address,
                          RedirectAttributes redirectAttributes) {
        try {
            // 创建用户账号
            User user = new User();
            user.setUsername(username);
            user.setPassword(password); // 实际应用中需要加密
            user.setEmail(email);
            user.setRole(User.UserRole.READER);
            user = userService.save(user);
            
            // 创建读者信息
            Reader reader = new Reader();
            reader.setName(name);
            reader.setPhone(phone);
            reader.setAddress(address);
            reader.setUser(user);
            reader.setMembershipStatus(Reader.MembershipStatus.ACTIVE);
            readerService.save(reader);
            
            redirectAttributes.addFlashAttribute("success", "读者添加成功");
            return "redirect:/readers";
            
        } catch (Exception e) {
            logger.error("Error adding reader: ", e);
            redirectAttributes.addFlashAttribute("error", "添加读者失败: " + e.getMessage());
            return "redirect:/admin/readers/new";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteReader(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            // 获取读者信息
            Reader reader = readerService.findById(id)
                .orElseThrow(() -> new RuntimeException("读者不存在"));

            // 获取关联的用户ID
            Long userId = reader.getUser().getId();

            // 先删除读者信息
            readerService.deleteById(id);

            // 再删除用户账号
            userService.deleteById(userId);

            redirectAttributes.addFlashAttribute("success", "读者删除成功");
            return "redirect:/readers";
        } catch (Exception e) {
            logger.error("Error deleting reader: ", e);
            redirectAttributes.addFlashAttribute("error", "删除读者失败: " + e.getMessage());
            return "redirect:/readers";
        }
    }
} 