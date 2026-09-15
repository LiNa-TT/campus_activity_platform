package com.example.campus.controller;

import com.example.campus.config.SecurityUtil;
import com.example.campus.entity.User;
import com.example.campus.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** 分类订阅：用户订阅活动分类，新活动发布自动推送消息。 */
@Controller
@RequiredArgsConstructor
public class SubscriptionController {
    private final SubscriptionService subscriptionService;
    private final SecurityUtil securityUtil;

    @GetMapping("/subscriptions")
    public String view(Model model) {
        User u = securityUtil.currentUser();
        model.addAttribute("categories", List.of("讲座", "社团", "竞赛", "校运会", "工会", "教职工文体"));
        model.addAttribute("mine", subscriptionService.mine(u.getId()));
        return "subscriptions";
    }

    @PostMapping("/subscriptions/subscribe")
    public String subscribe(@RequestParam String category) {
        User u = securityUtil.currentUser();
        subscriptionService.subscribe(u, category);
        return "redirect:/subscriptions";
    }

    @PostMapping("/subscriptions/unsubscribe")
    public String unsubscribe(@RequestParam String category) {
        User u = securityUtil.currentUser();
        subscriptionService.unsubscribe(u.getId(), category);
        return "redirect:/subscriptions";
    }
}
