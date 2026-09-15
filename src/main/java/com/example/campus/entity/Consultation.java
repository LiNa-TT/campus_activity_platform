package com.example.campus.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * ╔══════════════════════════════════════════════════════════════╗
 * ║  模块6 活动咨询管理实体【基础 CRUD：新增 / 编辑 / 删除】        ║
 * ╠══════════════════════════════════════════════════════════════╣
 * ║  【最简单模块】仅标题、内容、发布时间三个核心字段                ║
 * ║  【权限】ADMIN + EVENT_ADMIN 两个角色可管理                    ║
 * ║        （SecurityConfig /admin/consult/** 配置）              ║
 * ║  【公开端】/consultations 列表浏览 + /consultations/{id} 详情  ║
 * ╚══════════════════════════════════════════════════════════════╝
 */
@Entity
@Table(name = "consultation")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Consultation {

    /** ⭐主键：自增 ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 资讯标题（必填） */
    @Column(nullable = false, length = 120)
    private String title;

    /** 资讯正文内容 */
    @Column(length = 2000)
    private String content;

    /** 发布时间，默认取当前系统时间 */
    private LocalDateTime publishTime = LocalDateTime.now();
}
