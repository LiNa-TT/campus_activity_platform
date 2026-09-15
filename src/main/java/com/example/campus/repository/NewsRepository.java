package com.example.campus.repository;

import com.example.campus.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {
    List<News> findTop8ByOrderByTopDescPublishTimeDesc();
    List<News> findByCategoryOrderByPublishTimeDesc(String category);
    List<News> findAllByOrderByTopDescPublishTimeDesc();
}
