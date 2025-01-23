package com.service;

import com.model.Admin;
import java.util.List;
import java.util.Optional;

public interface AdminService {
    List<Admin> findAll();
    Optional<Admin> findById(Long id);
    Admin save(Admin admin);
    void deleteById(Long id);
    Admin findByUsername(String username);
}
