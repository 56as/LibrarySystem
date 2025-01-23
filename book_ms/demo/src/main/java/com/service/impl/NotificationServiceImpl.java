package com.service.impl;

import com.model.Notification;
import com.model.Reader;
import com.model.Borrowing;
import com.repository.NotificationRepository;
import com.repository.ReaderRepository;
import com.repository.BorrowingRepository;
import com.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ReaderRepository readerRepository;

    @Autowired
    private BorrowingRepository borrowingRepository;

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
    public List<Notification> getAllAnnouncements() {
        return notificationRepository.findByTypeOrderByCreateTimeDesc(Notification.NotificationType.ANNOUNCEMENT);
    }

    @Override
    public void createNotification(Notification notification) {
        notification.setCreateTime(LocalDateTime.now());
        notification.setRead(false);
        notificationRepository.save(notification);
    }

    @Override
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }

    @Override
    public void checkAndCreateOverdueNotifications(Long readerId) {
        // 获取读者的所有借阅记录
        List<Borrowing> borrowings = borrowingRepository.findByReader_IdAndStatus(readerId, Borrowing.BorrowingStatus.BORROWED);
        LocalDate today = LocalDate.now();

        for (Borrowing borrowing : borrowings) {
            // 计算距离到期还有多少天
            long daysUntilDue = ChronoUnit.DAYS.between(today, borrowing.getDueDate());
            
            // 如果还有3天或更少就到期，并且还没有发送过提醒
            if (daysUntilDue <= 3 && daysUntilDue >= 0) {
                // 检查是否已经存在相同的提醒
                List<Notification> existingNotifications = notificationRepository.findByTitleAndType(
                    "图书即将到期提醒", Notification.NotificationType.RETURN_REMINDER);
                
                if (existingNotifications.isEmpty()) {
                    Notification notification = new Notification();
                    notification.setReader(borrowing.getReader());
                    notification.setTitle("图书即将到期提醒");
                    notification.setContent("您借阅的《" + borrowing.getBook().getTitle() + "》将在" + daysUntilDue + "天后到期，请及时归还。");
                    notification.setCreateTime(LocalDateTime.now());
                    notification.setRead(false);
                    notification.setType(Notification.NotificationType.RETURN_REMINDER);
                    
                    notificationRepository.save(notification);
                }
            }
            // 如果已经逾期，创建逾期提醒
            else if (daysUntilDue < 0) {
                // 检查是否已经存在相同的提醒
                List<Notification> existingNotifications = notificationRepository.findByTitleAndType(
                    "图书逾期提醒", Notification.NotificationType.RETURN_REMINDER);
                
                if (existingNotifications.isEmpty()) {
                    Notification notification = new Notification();
                    notification.setReader(borrowing.getReader());
                    notification.setTitle("图书逾期提醒");
                    notification.setContent("您借阅的《" + borrowing.getBook().getTitle() + "》已逾期" + Math.abs(daysUntilDue) + "天，请尽快归还。");
                    notification.setCreateTime(LocalDateTime.now());
                    notification.setRead(false);
                    notification.setType(Notification.NotificationType.RETURN_REMINDER);
                    
                    notificationRepository.save(notification);
                }
            }
        }
    }
} 