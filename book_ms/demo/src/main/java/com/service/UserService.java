package com.service;

import com.model.User;
import java.util.Optional;
import java.util.List;

public interface UserService {
    List<User> findAll();
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    User save(User user);
    void deleteById(Long id);
}
