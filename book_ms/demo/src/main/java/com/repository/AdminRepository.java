package com.repository;

import com.model.Admin;
import com.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByUser_Username(String username);

    boolean existsByUser_Username(String username);
    
    Optional<Admin> findByUser(User user);
}

