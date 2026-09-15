package com.example.campus.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * ╔══════════════════════════════════════════════════════════════╗
 * ║  站内消息实体【订阅推送机制的"消息送达表"】                     ║
 * ╠══════════════════════════════════════════════════════════════╣
 * ║  【生成时机】ActivityService.save() 新增活动时自动批量生成      ║
 * ║  【消息去向】/messages 消息中心页面显示，导航栏未读徽标联动      ║
 * ║  【两种状态】isRead=false 未读 / true 已读                     ║
 * ╚══════════════════════════════════════════════════════════════╝
 */
@Entity
@Table(name = "message")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Message {

    /** ⭐主键：自增 ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** ⭐消息接收人：多对一外键，关联 app_user 表 */
    @ManyToOne(optional = false)
    private User user;

    /**
     * 关联的活动：可为空（纯系统通知时空）
     * 订阅推送场景下非空，指向刚发布的新活动
     */
    @ManyToOne
    private Activity activity;

    /** 消息所属分类，与 Activity.category 对应，方便统计筛选 */
    @Column(nullable = false, length = 30)
    private String category;

    /** 消息正文：例 "您订阅的【讲座】有新活动：人工智能前沿技术沙龙" */
    @Column(nullable = false, length = 200)
    private String content;

    /** 消息创建时间，默认当前时间 */
    private LocalDateTime createdTime = LocalDateTime.now();

    /**
     * 是否已读标记
     * false=未读（导航栏小红点/徽标显示依据）
     * true=已读
     */
    private Boolean isRead = false;
}
