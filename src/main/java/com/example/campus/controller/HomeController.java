package com.example.campus.controller;

import com.example.campus.service.ActivityService;
import com.example.campus.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final ActivityService activityService;
    private final NewsService newsService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("activities", activityService.recommended());
        model.addAttribute("news", newsService.latest8());
        return "index";
    }

    /** 活动中心：可视化列表 + 分页（每页6条）+ 分类/关键词筛选。 */
    @GetMapping("/activities")
    public String activities(@RequestParam(required = false) String keyword,
                             @RequestParam(required = false) String category,
                             @RequestParam(defaultValue = "1") int page,
                             Model model) {
        var result = activityService.page(keyword, category, PageRequest.of(Math.max(page - 1, 0), 6));
        model.addAttribute("page", result);
        model.addAttribute("activities", result.getContent());
        model.addAttribute("keyword", keyword);
        model.addAttribute("category", category);
        model.addAttribute("categories", java.util.List.of("讲座", "社团", "竞赛", "校运会", "工会", "教职工文体"));
        return "activities";
    }

    /** 活动详情页：按分类条件展示模块特有字段。 */
    @GetMapping("/activities/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("activity", activityService.get(id));
        return "activity-detail";
    }
}
