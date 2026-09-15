package com.example.campus.service;

import com.example.campus.entity.Subscription;
import com.example.campus.entity.User;
import com.example.campus.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** 分类订阅管理：通用共性功能——分类订阅、新活动消息自动推送提醒。 */
@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    public List<Subscription> mine(Long userId) {
        return subscriptionRepository.findByUserId(userId);
    }

    @Transactional
    public void subscribe(User user, String category) {
        if (category == null || category.isBlank()) return;
        if (!subscriptionRepository.existsByUserIdAndCategory(user.getId(), category)) {
            subscriptionRepository.save(Subscription.builder().user(user).category(category).build());
        }
    }

    @Transactional
    public void unsubscribe(Long userId, String category) {
        subscriptionRepository.deleteByUserIdAndCategory(userId, category);
    }
}
