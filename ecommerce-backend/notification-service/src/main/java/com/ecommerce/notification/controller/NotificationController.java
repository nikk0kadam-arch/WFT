package com.ecommerce.notification.controller;

import com.ecommerce.notification.dto.NotificationDTO;
import com.ecommerce.notification.service.NotificationService;
import com.ecommerce.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notification Management", description = "Notification endpoints")
@Slf4j
public class NotificationController {
    private final NotificationService notificationService;
    
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get all notifications for user")
    public ResponseEntity<ApiResponse<Page<NotificationDTO>>> getUserNotifications(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Fetching notifications for user: {}", userId);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<NotificationDTO> notifications = notificationService.getUserNotifications(userId, pageable);
        
        return ResponseEntity.ok(ApiResponse.success(notifications, "Notifications retrieved successfully"));
    }
    
    @GetMapping("/user/{userId}/unread")
    @Operation(summary = "Get unread notifications for user")
    public ResponseEntity<ApiResponse<Page<NotificationDTO>>> getUserUnreadNotifications(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Fetching unread notifications for user: {}", userId);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<NotificationDTO> notifications = notificationService.getUserUnreadNotifications(userId, pageable);
        
        return ResponseEntity.ok(ApiResponse.success(notifications, "Unread notifications retrieved successfully"));
    }
    
    @GetMapping("/user/{userId}/unread-count")
    @Operation(summary = "Get count of unread notifications")
    public ResponseEntity<ApiResponse<Long>> getUnreadCount(@PathVariable Long userId) {
        log.info("Fetching unread notification count for user: {}", userId);
        
        Long count = notificationService.getUnreadNotificationCount(userId);
        return ResponseEntity.ok(ApiResponse.success(count, "Unread count retrieved"));
    }
    
    @PutMapping("/{notificationId}/mark-as-read")
    @Operation(summary = "Mark notification as read")
    public ResponseEntity<ApiResponse<String>> markAsRead(@PathVariable Long notificationId) {
        log.info("Marking notification {} as read", notificationId);
        
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok(ApiResponse.success(null, "Notification marked as read"));
    }
    
    @PutMapping("/user/{userId}/mark-all-as-read")
    @Operation(summary = "Mark all notifications as read for user")
    public ResponseEntity<ApiResponse<String>> markAllAsRead(@PathVariable Long userId) {
        log.info("Marking all notifications as read for user: {}", userId);
        
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok(ApiResponse.success(null, "All notifications marked as read"));
    }
}
