package com.example.campus.service;

import com.example.campus.entity.Activity;
import com.example.campus.entity.Message;
import com.example.campus.entity.Subscription;
import com.example.campus.repository.ActivityRepository;
import com.example.campus.repository.MessageRepository;
import com.example.campus.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final MessageRepository messageRepository;

    public List<Activity> all() { return activityRepository.findAll(); }
    public Activity get(Long id) { return activityRepository.findById(id).orElseThrow(); }

    public Page<Activity> page(String keyword, String category, Pageable pageable) {
        boolean hasKw = keyword != null && !keyword.isBlank();
        boolean hasCat = category != null && !category.isBlank();
        if (hasKw && hasCat)
            return activityRepository.findByCategoryAndTitleContainingIgnoreCaseOrderByStartTimeDesc(category, keyword, pageable);
        if (hasCat)
            return activityRepository.findByCategoryOrderByStartTimeDesc(category, pageable);
        if (hasKw)
            return activityRepository.findByTitleContainingIgnoreCaseOrderByStartTimeDesc(keyword, pageable);
        return activityRepository.findAllByOrderByStartTimeDesc(pageable);
    }

    /** 发布/编辑活动；新增时按分类自动推送消息给订阅用户。 */
    @Transactional
    public Activity save(Activity a) {
        if (a.getEnrolledPeople() == null) a.setEnrolledPeople(0);
        if (a.getRecommended() == null) a.setRecommended(false);
        if (a.getArchived() == null) a.setArchived(false);
        if (a.getStatus() == null || a.getStatus().isBlank()) a.setStatus("报名中");
        boolean isNew = a.getId() == null;
        Activity saved = activityRepository.save(a);
        if (isNew && a.getCategory() != null) {
            pushToSubscribers(saved);
        }
        return saved;
    }

    private void pushToSubscribers(Activity a) {
        List<Subscription> subs = subscriptionRepository.findByCategory(a.getCategory());
        for (Subscription s : subs) {
            Message m = Message.builder()
                    .user(s.getUser())
                    .activity(a)
                    .category(a.getCategory())
                    .content("您订阅的【" + a.getCategory() + "】有新活动：" + a.getTitle())
                    .createdTime(LocalDateTime.now())
                    .isRead(false)
                    .build();
            messageRepository.save(m);
        }
    }

    public void delete(Long id) { activityRepository.deleteById(id); }

    public List<Activity> recommended() {
        return activityRepository.findTop6ByRecommendedTrueOrderByStartTimeAsc();
    }

    public long countByCategory(String category) {
        return activityRepository.countByCategory(category);
    }
}
