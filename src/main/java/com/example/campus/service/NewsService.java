package com.example.campus.service;

import com.example.campus.entity.News;
import com.example.campus.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/** 模块8 热点新闻推送：CRUD + 置顶 + 分类。 */
@Service
@RequiredArgsConstructor
public class NewsService {
    private final NewsRepository newsRepository;

    public List<News> latest8() {
        return newsRepository.findTop8ByOrderByTopDescPublishTimeDesc();
    }

    public List<News> all() {
        return newsRepository.findAllByOrderByTopDescPublishTimeDesc();
    }

    public List<News> byCategory(String category) {
        return newsRepository.findByCategoryOrderByPublishTimeDesc(category);
    }

    public News save(News n) {
        if (n.getPublishTime() == null) n.setPublishTime(LocalDateTime.now());
        if (n.getTop() == null) n.setTop(false);
        return newsRepository.save(n);
    }

    public void delete(Long id) {
        newsRepository.deleteById(id);
    }
}
