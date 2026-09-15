package com.example.campus.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * ╔══════════════════════════════════════════════════════════════╗
 * ║  报名记录实体【模块1 讲座签到+反馈、在线报名、统计的核心载体】 ║
 * ╠══════════════════════════════════════════════════════════════╣
 * ║  【DB 级唯一约束】activity_id + studentNo 联合唯一             ║
 * ║     → 即使 Service 校验被绕过，数据库也会拒绝重复报名           ║
 * ║     → Service 层 + DB 层双重保证，是业务正确性的最佳实践       ║
 * ║  【扩展字段】模块1 讲座额外延伸签到 + 反馈两栏                   ║
 * ║     其他模块默认值即可，不影响使用                               ║
 * ╚══════════════════════════════════════════════════════════════╝
 */
@Entity
@Table(name = "registration",
       uniqueConstraints = @UniqueConstraint(columnNames = {"activity_id", "studentNo"}))
       // ⭐⭐⭐ 数据库级唯一约束（联合唯一：同一个活动+同一个人只能有一条报名）
       //   作用：当 Service 层并发或被攻击者绕过时，DB 直接拒绝 → 防止脏数据
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Registration {

    /** ⭐主键：自增 ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * ⭐多对一外键：一条报名记录属于一个活动
     * optional=false → DB 层非空，必须有对应活动
     */
    @ManyToOne(optional = false)
    private Activity activity;

    /**
     * 报名人编号：统一指学号（学生）或工号（教职工）
     * 与 User.userNo 字段对应
     */
    @Column(nullable = false, length = 30)
    private String studentNo;

    /** 报名人姓名，冗余存一份便于统计列表直接显示不用联表 */
    @Column(nullable = false, length = 50)
    private String studentName;

    /** 报名时间 */
    private LocalDateTime registerTime;

    /** 报名状态：已报名 / 已取消 */
    private String status = "已报名";

    // ================================================================
    // 模块1 讲座扩展：签到 + 活动反馈
    // ================================================================
    /**
     * ⭐签到状态（模块1 讲座管理用）
     * 值：未签到 / 已签到 / 缺席
     * 管理员在 /admin/activity/stat/{id} 页面逐条更新
     */
    @Column(length = 20)
    private String attendance = "未签到";

    /** ⭐模块1 讲座：活动反馈评分，范围 1-5 星 */
    private Integer feedbackRating;

    /** 模块1 讲座：活动反馈文字评论，可选 */
    @Column(length = 500)
    private String feedbackComment;
}
