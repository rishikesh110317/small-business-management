package com.sbm.controller;

import com.sbm.entity.Notification;
import com.sbm.repository.NotificationRepository;
import com.sbm.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationRepository notificationRepository;

    @GetMapping
    public ResponseEntity<Page<Map<String, Object>>> getAll(@PageableDefault(size = 20) Pageable pageable) {
        Long userId = SecurityUtils.getCurrentUserId();
        Page<Map<String, Object>> page = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable)
                .map(n -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", n.getId()); m.put("title", n.getTitle());
                    m.put("message", n.getMessage()); m.put("type", n.getType().name());
                    m.put("isRead", n.getIsRead()); m.put("createdAt", n.getCreatedAt());
                    return m;
                });
        return ResponseEntity.ok(page);
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Long>> getUnreadCount() {
        long count = notificationRepository.countByUserIdAndIsReadFalse(SecurityUtils.getCurrentUserId());
        return ResponseEntity.ok(Map.of("count", count));
    }

    @PostMapping("/mark-all-read")
    @Transactional
    public ResponseEntity<Void> markAllRead() {
        notificationRepository.markAllAsRead(SecurityUtils.getCurrentUserId(), SecurityUtils.getCurrentBusinessId());
        return ResponseEntity.ok().build();
    }
}
