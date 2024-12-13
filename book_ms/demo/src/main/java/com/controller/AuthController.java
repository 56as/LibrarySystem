package com.controller;

import com.model.Reader;
import com.model.User;
import com.service.ReaderService;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private ReaderService readerService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user,
                             BindingResult result,
                             Model model) {
        // 检查用户名是否已存在
        if (userService.existsByUsername(user.getUsername())) {
            result.rejectValue("username", "error.user", "用户名已存在");
        }

        // 检查邮箱是否已存在
        if (userService.existsByEmail(user.getEmail())) {
            result.rejectValue("email", "error.user", "邮箱已被使用");
        }

        if (result.hasErrors()) {
            return "register";
        }

        // 设置默认值
        user.setRole(User.UserRole.READER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 保存用户
        User savedUser = userService.save(user);

        // 创建并保存对应的Reader实体
        Reader reader = new Reader();
        reader.setUser(savedUser);
        reader.setName(user.getUsername()); // 默认使用用户名作为读者姓名
        reader.setMembershipStatus(Reader.MembershipStatus.ACTIVE);
        readerService.save(reader);

        return "redirect:/login?registered";
    }
} 