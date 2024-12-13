package com.service.impl;

import com.model.User;
import com.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("开始验证用户登录: {}", username);
        
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> {
                        logger.error("用户不存在: {}", username);
                        return new UsernameNotFoundException("用户不存在: " + username);
                    });
            
            logger.info("找到用户: {}, 角色: {}", username, user.getRole());
            logger.info("用户详情 - ID: {}, 邮箱: {}, 密码: {}", user.getId(), user.getEmail(), user.getPassword());
            
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());
            logger.info("授予权限: {}", authority.getAuthority());
            
            UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(authority)
            );
            
            logger.info("用户认证信息已创建 - 用户名: {}, 权限: {}", 
                       userDetails.getUsername(), userDetails.getAuthorities());
            
            return userDetails;
        } catch (Exception e) {
            logger.error("验证用户时发生错误: {}", username, e);
            throw new UsernameNotFoundException("验证用户时发生错误: " + username, e);
        }
    }
} 