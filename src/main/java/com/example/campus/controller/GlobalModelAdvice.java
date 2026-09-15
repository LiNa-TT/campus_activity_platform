package com.example.campus.controller;

import com.example.campus.config.SecurityUtil;
import com.example.campus.entity.User;
import com.example.campus.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/** 向所有模板注入导航通用数据：当前用户、未读消息数、角色标识。 */
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAdvice {
    private final SecurityUtil securityUtil;
    private final MessageService messageService;

    @ModelAttribute
    public void commonAttributes(Model model) {
        User u = securityUtil.currentUser();
        model.addAttribute("currentUser", u);
        if (u != null) {
            model.addAttribute("unreadCount", messageService.unreadCount(u.getId()));
            model.addAttribute("isAdmin", securityUtil.hasRole("ADMIN"));
            model.addAttribute("isEventAdmin", securityUtil.hasRole("EVENT_ADMIN"));
            model.addAttribute("isClubLeader", securityUtil.hasRole("CLUB_LEADER"));
            model.addAttribute("isUnionAdmin", securityUtil.hasRole("UNION_ADMIN"));
            model.addAttribute("isTeacher", securityUtil.hasRole("TEACHER"));
            model.addAttribute("isAdminRole", securityUtil.hasRole("ADMIN")
                    || securityUtil.hasRole("EVENT_ADMIN")
                    || securityUtil.hasRole("CLUB_LEADER")
                    || securityUtil.hasRole("UNION_ADMIN"));
        } else {
            model.addAttribute("unreadCount", 0L);
        }
    }
}
