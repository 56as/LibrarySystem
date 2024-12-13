package com.repository;

import com.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 根据用户名查找用户
    Optional<User> findByUsername(String username);
    
    // 根据用户名和密码查找用户（用于登录验证）
    Optional<User> findByUsernameAndPassword(String username, String password);
    
    // 根据邮箱查找用户
    Optional<User> findByEmail(String email);
    
    // 检查用户名是否已存在
    boolean existsByUsername(String username);
    
    // 检查邮箱是否已存在
    boolean existsByEmail(String email);
}
