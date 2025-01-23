package com.service;

import com.model.Notification;
import java.util.List;

public interface NotificationService {
    List<Notification> getReaderNotifications(Long readerId);
    List<Notification> getUnreadNotifications(Long readerId);
    long getUnreadCount(Long readerId);
    void markAsRead(Long notificationId);
    void createReturnReminder(Long readerId, String bookTitle);
    List<Notification> getAllAnnouncements();
    void createNotification(Notification notification);
    void deleteNotification(Long id);
    void checkAndCreateOverdueNotifications(Long readerId);
}

