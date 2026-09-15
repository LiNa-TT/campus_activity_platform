package com.example.campus.controller;

import com.example.campus.config.SecurityUtil;
import com.example.campus.entity.User;
import com.example.campus.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final SecurityUtil securityUtil;

    /** 我的消息：展示订阅推送提醒。 */
    @GetMapping("/messages")
    public String messages(Model model) {
        User u = securityUtil.currentUser();
        model.addAttribute("messages", messageService.mine(u.getId()));
        return "messages";
    }

    @PostMapping("/messages/{id}/read")
    public String markRead(@PathVariable Long id) {
        messageService.markRead(id);
        return "redirect:/messages";
    }

    @PostMapping("/messages/read-all")
    public String markAllRead() {
        User u = securityUtil.currentUser();
        messageService.markAllRead(u.getId());
        return "redirect:/messages";
    }
}
