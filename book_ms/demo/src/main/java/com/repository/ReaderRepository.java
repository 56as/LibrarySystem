package com.repository;

import com.model.Reader;
import com.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReaderRepository extends JpaRepository<Reader, Long> {
    // 根据姓名查找读者
    List<Reader> findByNameContaining(String name);
    
    // 根据电话查找读者
    Optional<Reader> findByPhone(String phone);
    
    // 根据会员状态查找读者
    List<Reader> findByMembershipStatus(Reader.MembershipStatus status);
    
    // 检查电话是否已存在
    boolean existsByPhone(String phone);
    
    Optional<Reader> findByUser(User user);
}
