package com.service.impl;

import com.model.Notification;
import com.model.Reader;
import com.repository.NotificationRepository;
import com.repository.ReaderRepository;
import com.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ReaderRepository readerRepository;

    @Override
    public List<Notification> getReaderNotifications(Long readerId) {
        return notificationRepository.findByReaderIdOrderByCreateTimeDesc(readerId);
    }

    @Override
    public List<Notification> getUnreadNotifications(Long readerId) {
        return notificationRepository.findByReaderIdAndIsReadOrderByCreateTimeDesc(readerId, false);
    }

    @Override
    public long getUnreadCount(Long readerId) {
        return notificationRepository.countByReaderIdAndIsRead(readerId, false);
    }

    @Override
    public void markAsRead(Long notificationId) {
        notificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }

    @Override
    public void createReturnReminder(Long readerId, String bookTitle) {
        Reader reader = readerRepository.findById(readerId)
                .orElseThrow(() -> new IllegalArgumentException("Reader not found"));

        Notification notification = new Notification();
        notification.setReader(reader);
        notification.setTitle("图书归还提醒");
        notification.setContent("您借阅的《" + bookTitle + "》即将到期，请及时归还。");
        notification.setCreateTime(LocalDateTime.now());
        notification.setRead(false);
        notification.setType(Notification.NotificationType.RETURN_REMINDER);

        notificationRepository.save(notification);
    }

    @Override
    public void createSystemMaintenanceNotice(Long readerId, String title, String content) {
        Reader reader = readerRepository.findById(readerId)
                .orElseThrow(() -> new IllegalArgumentException("Reader not found"));

        Notification notification = new Notification();
        notification.setReader(reader);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setCreateTime(LocalDateTime.now());
        notification.setRead(false);
        notification.setType(Notification.NotificationType.SYSTEM_MAINTENANCE);

        notificationRepository.save(notification);
    }
} 