package com.example.campus.service;

import com.example.campus.entity.Message;
import com.example.campus.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** 消息提醒服务：展示订阅推送、已读管理。 */
@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;

    public List<Message> mine(Long userId) {
        return messageRepository.findByUserIdOrderByCreatedTimeDesc(userId);
    }

    public long unreadCount(Long userId) {
        return messageRepository.countByUserIdAndIsReadFalse(userId);
    }

    @Transactional
    public void markRead(Long id) {
        messageRepository.findById(id).ifPresent(m -> {
            m.setIsRead(true);
            messageRepository.save(m);
        });
    }

    @Transactional
    public void markAllRead(Long userId) {
        messageRepository.findByUserIdOrderByCreatedTimeDesc(userId).forEach(m -> {
            if (!Boolean.TRUE.equals(m.getIsRead())) {
                m.setIsRead(true);
                messageRepository.save(m);
            }
        });
    }
}
