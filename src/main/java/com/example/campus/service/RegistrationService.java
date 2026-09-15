package com.example.campus.service;

import com.example.campus.entity.*;
import com.example.campus.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final RegistrationRepository registrationRepository;
    private final ActivityRepository activityRepository;

    @Transactional
    public String register(Long activityId, String studentNo, String studentName) {
        Activity a = activityRepository.findById(activityId).orElseThrow();
        if (registrationRepository.existsByActivityIdAndStudentNo(activityId, studentNo))
            return "你已经报名过该活动";
        if (a.getMaxPeople() != null && a.getEnrolledPeople() >= a.getMaxPeople())
            return "报名人数已满";
        if (!"报名中".equals(a.getStatus()))
            return "当前活动不可报名";
        if (a.getRegistrationDeadline() != null && LocalDateTime.now().isAfter(a.getRegistrationDeadline()))
            return "报名已截止";

        Registration r = Registration.builder()
                .activity(a).studentNo(studentNo).studentName(studentName)
                .registerTime(LocalDateTime.now()).status("已报名").attendance("未签到").build();
        registrationRepository.save(r);
        a.setEnrolledPeople(a.getEnrolledPeople() + 1);
        activityRepository.save(a);
        return "报名成功";
    }

    public List<Registration> mine(String studentNo) {
        return registrationRepository.findByStudentNoOrderByRegisterTimeDesc(studentNo);
    }

    public List<Registration> byActivity(Long activityId) {
        return registrationRepository.findByActivityIdOrderByRegisterTimeDesc(activityId);
    }

    public long countByActivity(Long activityId) {
        return registrationRepository.countByActivityId(activityId);
    }

    /** 模块1 讲座：管理签到记录 */
    @Transactional
    public void updateAttendance(Long registrationId, String attendance) {
        Registration r = registrationRepository.findById(registrationId).orElseThrow();
        r.setAttendance(attendance);
        registrationRepository.save(r);
    }

    /** 模块1 讲座：收集活动反馈 */
    @Transactional
    public String submitFeedback(Long registrationId, Integer rating, String comment) {
        Registration r = registrationRepository.findById(registrationId).orElseThrow();
        if (rating == null || rating < 1 || rating > 5) return "评分需在1-5之间";
        r.setFeedbackRating(rating);
        r.setFeedbackComment(comment);
        registrationRepository.save(r);
        return "反馈提交成功";
    }

    public long countAttendance(Long activityId, String attendance) {
        return registrationRepository.countByActivityIdAndAttendance(activityId, attendance);
    }
}
