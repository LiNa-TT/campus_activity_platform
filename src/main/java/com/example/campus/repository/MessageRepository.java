package com.example.campus.repository;

import com.example.campus.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByUserIdOrderByCreatedTimeDesc(Long userId);
    long countByUserIdAndIsReadFalse(Long userId);
}
