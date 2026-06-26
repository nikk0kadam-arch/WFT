package com.ecommerce.notification.service;

import com.ecommerce.notification.dto.NotificationDTO;
import com.ecommerce.notification.entity.Notification;
import com.ecommerce.notification.repository.NotificationRepository;
import com.ecommerce.shared.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class NotificationService {
    
    private final NotificationRepository notificationRepository;
    
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }
    
    public NotificationDTO createNotification(Long userId, String type, String title, String message) {
        log.info("Creating notification for user: {}, type: {}", userId, type);
        
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setType(type);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setChannel("IN_APP");
        notification.setStatus("SENT");
        notification.setRead(false);
        
        Notification savedNotification = notificationRepository.save(notification);
        return convertToDTO(savedNotification);
    }
    
    public Page<NotificationDTO> getUserNotifications(Long userId, Pageable pageable) {
        log.info("Fetching notifications for user: {}", userId);
        
        return notificationRepository.findByUserId(userId, pageable)
                .map(this::convertToDTO);
    }
    
    public Page<NotificationDTO> getUserUnreadNotifications(Long userId, Pageable pageable) {
        log.info("Fetching unread notifications for user: {}", userId);
        
        return notificationRepository.findByUserIdAndRead(userId, false, pageable)
                .map(this::convertToDTO);
    }
    
    public Long getUnreadNotificationCount(Long userId) {
        return (long) notificationRepository.findByUserIdAndReadFalse(userId).size();
    }
    
    public void markAsRead(Long notificationId) {
        log.info("Marking notification {} as read", notificationId);
        
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + notificationId));
        
        notification.setRead(true);
        notificationRepository.save(notification);
    }
    
    public void markAllAsRead(Long userId) {
        log.info("Marking all notifications as read for user: {}", userId);
        
        notificationRepository.findByUserIdAndReadFalse(userId)
                .forEach(notification -> {
                    notification.setRead(true);
                    notificationRepository.save(notification);
                });
    }
    
    private NotificationDTO convertToDTO(Notification notification) {
        return new NotificationDTO(
                notification.getId(),
                notification.getUserId(),
                notification.getType(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getRead(),
                notification.getChannel(),
                notification.getStatus()
        );
    }
}
