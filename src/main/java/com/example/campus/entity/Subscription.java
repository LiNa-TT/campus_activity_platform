package com.example.campus.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * ╔══════════════════════════════════════════════════════════════╗
 * ║  分类订阅实体【消息自动推送机制的"用户偏好表"】                 ║
 * ╠══════════════════════════════════════════════════════════════╣
 * ║  【工作流程】                                                  ║
 * ║     1. 用户在 /subscriptions 页面订阅"讲座"分类                ║
 * ║        → 生成一条 Subscription(user=xxx, category=讲座)       ║
 * ║     2. 管理员在后台发布新的讲座活动                             ║
 * ║        → ActivityService.save() 检测 isNew=true               ║
 * ║        → 调用 subscriptionRepository.findByCategory("讲座")   ║
 * ║        → 遍历结果每个用户生成 Message 站内消息                 ║
 * ║  【DB 级唯一约束】user_id + category 联合唯一                   ║
 * ║     → 同一用户对同一分类不能重复订阅                            ║
 * ╚══════════════════════════════════════════════════════════════╝
 */
@Entity
@Table(name = "subscription",
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "category"}))
       // ⭐联合唯一：防止用户狂点"订阅"生成多条相同记录
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Subscription {

    /** ⭐主键：自增 ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** ⭐订阅者：多对一外键，关联 app_user 表 */
    @ManyToOne(optional = false)
    private User user;

    /**
     * 订阅的活动分类
     * 值：讲座 / 社团 / 竞赛 / 校运会 / 工会 / 教职工文体
     * 与 Activity.category 字段严格对应
     */
    @Column(nullable = false, length = 30)
    private String category;
}
