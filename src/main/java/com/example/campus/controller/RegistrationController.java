package com.example.campus.controller;

import com.example.campus.config.SecurityUtil;
import com.example.campus.entity.User;
import com.example.campus.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;
    private final SecurityUtil securityUtil;

    /** 普通用户在线报名：自动带入当前登录用户编号与姓名。 */
    @PostMapping("/activity/register")
    public String register(@RequestParam Long activityId,
                           @RequestParam(required = false) String studentNo,
                           @RequestParam(required = false) String studentName,
                           RedirectAttributes ra) {
        User u = securityUtil.currentUser();
        if (u == null) return "redirect:/login";
        String no = (studentNo == null || studentNo.isBlank()) ? u.getUserNo() : studentNo;
        String name = (studentName == null || studentName.isBlank()) ? u.getName() : studentName;
        String message = registrationService.register(activityId, no, name);
        ra.addFlashAttribute("message", message);
        ra.addFlashAttribute("activityId", activityId);
        return "redirect:/register-result";
    }

    @GetMapping("/register-result")
    public String registerResult() {
        return "register-result";
    }

    /** 我的报名：按当前登录用户编号查询。 */
    @GetMapping("/my-registrations")
    public String mine(Model model) {
        User u = securityUtil.currentUser();
        model.addAttribute("registrations", u == null ? java.util.List.of() : registrationService.mine(u.getUserNo()));
        return "my-registrations";
    }

    /** 模块1 讲座：用户提交活动反馈。 */
    @PostMapping("/activity/feedback")
    public String feedback(@RequestParam Long registrationId,
                           @RequestParam Integer rating,
                           @RequestParam(required = false) String comment,
                           RedirectAttributes ra) {
        String msg = registrationService.submitFeedback(registrationId, rating, comment);
        ra.addFlashAttribute("message", msg);
        return "redirect:/my-registrations";
    }
}
