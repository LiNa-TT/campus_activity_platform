package com.example.campus.controller;

import com.example.campus.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/** 模块8 热点新闻：公众浏览端（无需登录）。 */
@Controller
@RequiredArgsConstructor
public class NewsController {
    private final NewsService newsService;

    @GetMapping("/news")
    public String list(@RequestParam(required = false) String category, Model model) {
        model.addAttribute("categories", java.util.List.of("校园通知", "教育热点", "时政新闻", "竞赛通知", "就业资讯"));
        model.addAttribute("currentCategory", category);
        model.addAttribute("newsList", category == null || category.isBlank()
                ? newsService.all() : newsService.byCategory(category));
        return "news";
    }

    @GetMapping("/news/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("news", newsService.all().stream()
                .filter(n -> n.getId().equals(id)).findFirst().orElseThrow());
        return "news-detail";
    }
}
