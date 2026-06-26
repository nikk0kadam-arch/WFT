package com.ecommerce.notification.repository;

import com.ecommerce.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findByUserId(Long userId, Pageable pageable);
    Page<Notification> findByUserIdAndRead(Long userId, Boolean read, Pageable pageable);
    List<Notification> findByUserIdAndReadFalse(Long userId);
}
