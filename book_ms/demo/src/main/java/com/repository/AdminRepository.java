package com.repository;

import com.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    // Find admin by associated user's username
    Optional<Admin> findByUser_Username(String username);
    
    // Check if admin exists with given user's username
    boolean existsByUser_Username(String username);
}
