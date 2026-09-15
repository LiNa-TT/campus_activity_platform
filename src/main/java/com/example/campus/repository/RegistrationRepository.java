package com.example.campus.repository;

import com.example.campus.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    boolean existsByActivityIdAndStudentNo(Long activityId, String studentNo);
    List<Registration> findByStudentNoOrderByRegisterTimeDesc(String studentNo);
    long countByActivityId(Long activityId);
    List<Registration> findByActivityIdOrderByRegisterTimeDesc(Long activityId);
    long countByActivityIdAndAttendance(Long activityId, String attendance);
}
