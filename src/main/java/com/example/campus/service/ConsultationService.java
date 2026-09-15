package com.example.campus.service;

import com.example.campus.entity.Consultation;
import com.example.campus.repository.ConsultationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/** 模块6 活动咨询管理：基础新增/编辑/删除。 */
@Service
@RequiredArgsConstructor
public class ConsultationService {
    private final ConsultationRepository consultationRepository;

    public List<Consultation> all() {
        return consultationRepository.findAllByOrderByPublishTimeDesc();
    }

    public Consultation get(Long id) {
        return consultationRepository.findById(id).orElseThrow();
    }

    public Consultation save(Consultation c) {
        if (c.getPublishTime() == null) c.setPublishTime(LocalDateTime.now());
        return consultationRepository.save(c);
    }

    public void delete(Long id) {
        consultationRepository.deleteById(id);
    }
}
