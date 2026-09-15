package com.example.campus.repository;

import com.example.campus.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByCategory(String category);
    List<Subscription> findByUserId(Long userId);
    boolean existsByUserIdAndCategory(Long userId, String category);
    void deleteByUserIdAndCategory(Long userId, String category);
}
