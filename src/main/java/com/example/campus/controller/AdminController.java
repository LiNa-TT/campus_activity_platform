package com.example.campus.controller;

import com.example.campus.config.SecurityUtil;
import com.example.campus.entity.Activity;
import com.example.campus.entity.User;
import com.example.campus.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

/**
 * 后台管理：统一处理活动类模块（1讲座/2社团/3竞赛/4校运会/5工会/7教职工文体）的
 * 发布/编辑/删除、报名统计、签到与反馈管理，以及模块6咨询、模块8新闻、模块5归档。
 * 按角色限制可管理的活动分类，实现多角色区分。
 */
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final ActivityService activityService;
    private final RegistrationService registrationService;
    private final NewsService newsService;
    private final ConsultationService consultationService;
    private final SecurityUtil securityUtil;

    /** 各角色可管理的活动分类。 */
    private List<String> allowedCategories(User u) {
        if (u == null) return List.of();
        return switch (u.getRole()) {
            case "ADMIN" -> List.of("讲座", "社团", "竞赛", "校运会", "工会", "教职工文体");
            case "EVENT_ADMIN" -> List.of("讲座", "竞赛", "校运会", "教职工文体");
            case "CLUB_LEADER" -> List.of("社团");
            case "UNION_ADMIN" -> List.of("工会", "教职工文体");
            default -> List.of();
        };
    }

    private boolean canManage(User u, String category) {
        return u != null && allowedCategories(u).contains(category);
    }

    @GetMapping
    public String admin(@RequestParam(required = false) Long edit, Model model) {
        User u = securityUtil.currentUser();
        List<String> allowed = allowedCategories(u);
        List<Activity> activities = activityService.all().stream()
                .filter(a -> allowed.contains(a.getCategory()))
                .toList();
        model.addAttribute("activities", activities);
        model.addAttribute("allowedCategories", allowed);
        model.addAttribute("allCategories", List.of("讲座", "社团", "竞赛", "校运会", "工会", "教职工文体"));
        model.addAttribute("editActivity", edit == null ? null : activityService.get(edit));
        // ADMIN 可管理新闻与咨询
        if ("ADMIN".equals(u.getRole())) {
            model.addAttribute("newsList", newsService.all());
            model.addAttribute("consultations", consultationService.all());
        }
        return "admin";
    }

    /** 发布/编辑活动。 */
    @PostMapping("/activity/save")
    public String saveActivity(Activity a, RedirectAttributes ra) {
        User u = securityUtil.currentUser();
        if (!canManage(u, a.getCategory())) {
            ra.addFlashAttribute("error", "无权发布该分类活动：" + a.getCategory());
            return "redirect:/admin";
        }
        activityService.save(a);
        ra.addFlashAttribute("message", "活动保存成功");
        return "redirect:/admin";
    }

    @PostMapping("/activity/delete/{id}")
    public String deleteActivity(@PathVariable Long id, RedirectAttributes ra) {
        User u = securityUtil.currentUser();
        Activity a = activityService.get(id);
        if (!canManage(u, a.getCategory())) {
            ra.addFlashAttribute("error", "无权删除该活动");
            return "redirect:/admin";
        }
        activityService.delete(id);
        ra.addFlashAttribute("message", "活动已删除");
        return "redirect:/admin";
    }

    /** 报名人数统计管理（通用共性功能）。模块1讲座额外展示签到统计与反馈。 */
    @GetMapping("/activity/stat/{id}")
    public String stat(@PathVariable Long id, Model model) {
        Activity a = activityService.get(id);
        model.addAttribute("activity", a);
        model.addAttribute("registrations", registrationService.byActivity(id));
        model.addAttribute("total", registrationService.countByActivity(id));
        model.addAttribute("signedIn", registrationService.countAttendance(id, "已签到"));
        model.addAttribute("absent", registrationService.countAttendance(id, "缺席"));
        return "admin-stat";
    }

    /** 模块1讲座：管理签到记录。 */
    @PostMapping("/activity/signin")
    public String signIn(@RequestParam Long registrationId,
                         @RequestParam String attendance,
                         @RequestParam Long activityId,
                         RedirectAttributes ra) {
        registrationService.updateAttendance(registrationId, attendance);
        ra.addFlashAttribute("message", "签到状态已更新");
        return "redirect:/admin/activity/stat/" + activityId;
    }

    // ===== 模块8 热点新闻 CRUD + 置顶（仅 ADMIN） =====
    @PostMapping("/news/save")
    public String saveNews(com.example.campus.entity.News n, RedirectAttributes ra) {
        newsService.save(n);
        ra.addFlashAttribute("message", "新闻已保存");
        return "redirect:/admin";
    }

    @PostMapping("/news/delete/{id}")
    public String deleteNews(@PathVariable Long id) {
        newsService.delete(id);
        return "redirect:/admin";
    }

    @PostMapping("/news/top/{id}")
    public String toggleTop(@PathVariable Long id) {
        com.example.campus.entity.News n = newsService.all().stream()
                .filter(x -> x.getId().equals(id)).findFirst().orElseThrow();
        n.setTop(!Boolean.TRUE.equals(n.getTop()));
        newsService.save(n);
        return "redirect:/admin";
    }

    // ===== 模块6 活动咨询 CRUD（ADMIN/EVENT_ADMIN） =====
    @PostMapping("/consult/save")
    public String saveConsult(com.example.campus.entity.Consultation c, RedirectAttributes ra) {
        consultationService.save(c);
        ra.addFlashAttribute("message", "资讯已保存");
        return "redirect:/admin";
    }

    @PostMapping("/consult/delete/{id}")
    public String deleteConsult(@PathVariable Long id) {
        consultationService.delete(id);
        return "redirect:/admin";
    }

    // ===== 模块5 工会活动归档管理（ADMIN/UNION_ADMIN） =====
    @GetMapping("/union")
    public String union(Model model) {
        model.addAttribute("activities", activityService.all().stream()
                .filter(a -> "工会".equals(a.getCategory()) || "教职工文体".equals(a.getCategory()))
                .toList());
        // 各活动报名统计
        Map<Long, Long> stat = new java.util.HashMap<>();
        activityService.all().forEach(a -> stat.put(a.getId(), registrationService.countByActivity(a.getId())));
        model.addAttribute("stat", stat);
        return "admin-union";
    }

    @PostMapping("/union/archive/{id}")
    public String archive(@PathVariable Long id, RedirectAttributes ra) {
        Activity a = activityService.get(id);
        a.setArchived(!Boolean.TRUE.equals(a.getArchived()));
        activityService.save(a);
        ra.addFlashAttribute("message", a.getArchived() ? "已归档" : "已取消归档");
        return "redirect:/admin/union";
    }
}
