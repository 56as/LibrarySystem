package com.repository;

import com.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByReaderIdOrderByCreateTimeDesc(Long readerId);
    List<Notification> findByReaderIdAndIsReadOrderByCreateTimeDesc(Long readerId, boolean isRead);
    long countByReaderIdAndIsRead(Long readerId, boolean isRead);
} 