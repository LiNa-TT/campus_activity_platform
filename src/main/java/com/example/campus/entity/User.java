package com.example.campus.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * ╔══════════════════════════════════════════════════════════════╗
 * ║  平台用户实体（核心身份认证数据载体）                          ║
 * ╠══════════════════════════════════════════════════════════════╣
 * ║  【表名说明】app_user —— 避免使用 MySQL 保留字 user           ║
 * ║  【六种角色】对应 role 字段值：                                 ║
 * ║    STUDENT        —— 学生（浏览、报名、订阅、反馈）             ║
 * ║    TEACHER        —— 教职工（浏览工会/教职工文体、报名）       ║
 * ║    CLUB_LEADER    —— 社团负责人（仅能管理社团活动）            ║
 * ║    EVENT_ADMIN    —— 赛事管理员（讲座/竞赛/校运会/教职工文体） ║
 * ║    UNION_ADMIN    —— 工会管理员（工会/教职工文体 + 归档）      ║
 * ║    ADMIN          —— 后台管理员（全部模块 + 新闻 + 资讯）      ║
 * ║  【联动】role 字段 ↔ SecurityConfig URL 权限 ↔ AdminController ║
 * ║        allowedCategories() 共同组成双层权限隔离体系            ║
 * ╚══════════════════════════════════════════════════════════════╝
 */
@Entity
@Table(name = "app_user")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {

    /** ⭐主键：自增 ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** ⭐用户名：登录凭据，DB 级唯一约束 + 非空 */
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    /** ⭐密码：存 BCrypt 加密后的哈希值（长度必须 ≥60 容纳密文） */
    @Column(nullable = false, length = 100)
    private String password;

    /** 真实姓名：显示在导航栏、报名信息里 */
    @Column(nullable = false, length = 50)
    private String name;

    /**
     * ⭐⭐⭐ 角色标识（权限体系的核心字段）
     * 值：STUDENT / TEACHER / CLUB_LEADER / EVENT_ADMIN / UNION_ADMIN / ADMIN
     * 被以下三处共同引用：
     *   1. SecurityConfig.filterChain() → URL 级粗粒度权限
     *   2. AdminController.allowedCategories() → 业务级分类隔离
     *   3. Thymeleaf sec:authorize → 前端视图显隐
     */
    @Column(nullable = false, length = 30)
    private String role;

    /**
     * 学号（学生）或工号（教职工）统一编号
     * 报名时作为 studentNo 字段值自动带入，无需用户重复填写
     */
    @Column(length = 30)
    private String userNo;
}
