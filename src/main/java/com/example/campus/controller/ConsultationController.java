package com.example.campus.controller;

import com.example.campus.service.ConsultationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/** 模块6 活动咨询：公众浏览端。 */
@Controller
@RequiredArgsConstructor
public class ConsultationController {
    private final ConsultationService consultationService;

    @GetMapping("/consultations")
    public String list(Model model) {
        model.addAttribute("consultations", consultationService.all());
        return "consultations";
    }

    @GetMapping("/consultations/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("consultation", consultationService.get(id));
        return "consultation-detail";
    }
}
