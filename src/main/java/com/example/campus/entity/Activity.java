package com.example.campus.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * ╔═══════════════════════════════════════════════════════════════════╗
 * ║  统一活动实体【本项目架构最核心设计亮点】                            ║
 * ╠═══════════════════════════════════════════════════════════════════╣
 * ║  【核心思想】一张 activity 表 + category 分类字段 = 覆盖 6 类活动  ║
 * ║     讲座/社团/竞赛/校运会/工会/教职工文体                           ║
 * ║  【好处】                                                         ║
 * ║    ① 不做 6 张结构几乎一样的表 → 避免"实体类爆炸"                  ║
 * ║    ② 各模块特有字段设为可空（nullable），按分类在视图层条件展示    ║
 * ║    ③ 新增第 N 类活动时，只需加一个 category 枚举值，零实体改动     ║
 * ║  【字段分区】                                                     ║
 * ║    通用字段：所有活动共用（标题、时间、地点、人数、海报…）          ║
 * ║    模块1 讲座：speaker 主讲人 / department 所属院系                ║
 * ║    模块3 竞赛：requirements 要求 / registrationDeadline 截止       ║
 * ║                scheduleInfo 赛程安排                               ║
 * ║    模块4 校运会 + 模块7 教职工文体：resultsInfo 成绩 / awardsInfo 表彰║
 * ║    模块5 工会：archived 归档标记                                  ║
 * ╚═══════════════════════════════════════════════════════════════════╝
 */
@Entity
@Table(name = "activity")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Activity {

    /** ⭐主键：自增 ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 活动标题（必填） */
    @Column(nullable = false, length = 100)
    private String title;

    /**
     * ⭐⭐⭐【分类字段】本实体设计的灵魂
     * 值：讲座 / 社团 / 竞赛 / 校运会 / 工会 / 教职工文体
     * 决定了：
     *   - 首页/活动中心筛选分类（HomeController）
     *   - 管理端角色可见范围（AdminController.allowedCategories）
     *   - 订阅推送匹配维度（ActivityService.pushToSubscribers）
     *   - 详情页显示哪些特有字段（activity-detail.html Thymeleaf 条件）
     */
    @Column(length = 30)
    private String category;

    /** 主办方 */
    @Column(length = 100)
    private String organizer;

    /** 活动地点 */
    @Column(length = 150)
    private String location;

    /** 活动开始时间 */
    private LocalDateTime startTime;

    /** 活动结束时间 */
    private LocalDateTime endTime;

    /** ⭐人数上限：报名校验用，超过则"报名人数已满" */
    private Integer maxPeople;

    /** ⭐已报名人数：报名成功后 +1，配合 maxPeople 做容量控制 */
    private Integer enrolledPeople = 0;

    /** 活动描述：富文本纯文本均可用 */
    @Column(length = 1000)
    private String description;

    /** 活动海报图片 URL：社团/竞赛/校运会等使用 */
    @Column(length = 500)
    private String coverUrl;

    /** 活动状态：报名中 / 已结束 / 已截止，报名校验用 */
    @Column(length = 20)
    private String status = "报名中";

    /** 推荐标记：true 的活动会出现在首页推荐位（前6条） */
    private Boolean recommended = false;

    // ================================================================
    // 模块1 讲座（学术讲座）特有的扩展字段
    // ================================================================
    /** 模块1 讲座：主讲人姓名 */
    @Column(length = 50)
    private String speaker;
    /** 模块1 讲座：主办院系，如"计算机学院" */
    @Column(length = 80)
    private String department;

    // ================================================================
    // 模块3 竞赛（学科竞赛、双创、程序设计）特有的扩展字段
    // ================================================================
    /** 模块3 竞赛：参赛要求（如语言要求、年级限制、作品要求等） */
    @Column(length = 1000)
    private String requirements;
    /** ⭐模块3 竞赛：报名截止时间（报名校验第4重） */
    private LocalDateTime registrationDeadline;
    /** 模块3 竞赛：赛事流程安排（初赛-复赛-决赛时间表） */
    @Column(length = 1000)
    private String scheduleInfo;

    // ================================================================
    // 模块4 校运会 + 模块7 教职工大型文体 共用扩展字段
    // ================================================================
    /** 赛后成绩公示：如"男子100米 张三 10.5s 冠军" */
    @Column(length = 2000)
    private String resultsInfo;
    /** 表彰公告：如"团体总分第一名：计算机学院" */
    @Column(length = 2000)
    private String awardsInfo;

    // ================================================================
    // 模块5 工会活动 归档标记
    // ================================================================
    /**
     * ⭐模块5 工会：活动归档标记
     * true = 已归档（历史活动），false = 正常进行中
     * 工会管理员在 /admin/union 页面对此进行切换管理
     */
    private Boolean archived = false;
}
