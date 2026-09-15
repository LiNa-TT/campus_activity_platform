package com.example.campus.repository;

import com.example.campus.entity.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findTop6ByRecommendedTrueOrderByStartTimeAsc();
    List<Activity> findByCategoryOrderByStartTimeDesc(String category);
    List<Activity> findByTitleContainingIgnoreCaseOrderByStartTimeDesc(String keyword);

    Page<Activity> findAllByOrderByStartTimeDesc(Pageable pageable);
    Page<Activity> findByCategoryOrderByStartTimeDesc(String category, Pageable pageable);
    Page<Activity> findByTitleContainingIgnoreCaseOrderByStartTimeDesc(String keyword, Pageable pageable);
    Page<Activity> findByCategoryAndTitleContainingIgnoreCaseOrderByStartTimeDesc(String category, String keyword, Pageable pageable);

    long countByCategory(String category);
}
