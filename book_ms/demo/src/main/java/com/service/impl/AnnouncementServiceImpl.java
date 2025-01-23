package com.service.impl;

import com.model.Announcement;
import com.model.Notification;
import com.model.Reader;
import com.repository.AnnouncementRepository;
import com.repository.NotificationRepository;
import com.repository.ReaderRepository;
import com.service.AnnouncementService;
import com.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AnnouncementServiceImpl implements AnnouncementService {
    
    private static final Logger logger = LoggerFactory.getLogger(AnnouncementServiceImpl.class);

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private ReaderRepository readerRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public List<Announcement> findAll() {
        return announcementRepository.findAllByOrderByCreateTimeDesc();
    }

    @Override
    public Announcement save(Announcement announcement) {
        try {
            logger.info("开始保存公告: {}", announcement.getTitle());
            
            // 保存公告
            Announcement savedAnnouncement = announcementRepository.save(announcement);
            logger.info("公告保存成功，ID: {}", savedAnnouncement.getId());
            
            // 获取所有读者
            List<Reader> readers = readerRepository.findAll();
            logger.info("找到 {} 个读者需要发送通知", readers.size());
            
            // 为所有读者创建通知
            for (Reader reader : readers) {
                try {
                    Notification notification = new Notification();
                    notification.setReader(reader);
                    notification.setTitle("新公告：" + announcement.getTitle());
                    notification.setContent(announcement.getContent());
                    notification.setType(Notification.NotificationType.ANNOUNCEMENT);
                    
                    notificationService.createNotification(notification);
                    logger.info("成功为读者 {} 创建通知", reader.getId());
                } catch (Exception e) {
                    logger.error("为读者 {} 创建通知失败: {}", reader.getId(), e.getMessage(), e);
                }
            }
            
            return savedAnnouncement;
        } catch (Exception e) {
            logger.error("保存公告失败: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        try {
            logger.info("开始删除公告，ID: {}", id);
            
            // 获取公告信息
            Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("公告不存在"));
            
            // 删除相关的通知
            List<Notification> notifications = notificationRepository.findByTitleAndType(
                "新公告：" + announcement.getTitle(),
                Notification.NotificationType.ANNOUNCEMENT
            );
            
            if (!notifications.isEmpty()) {
                logger.info("删除公告相关的 {} 条通知", notifications.size());
                notificationRepository.deleteAll(notifications);
            }
            
            // 删除公告
            announcementRepository.deleteById(id);
            logger.info("公告删除成功");
        } catch (Exception e) {
            logger.error("删除公告失败: {}", e.getMessage(), e);
            throw e;
        }
    }
}
