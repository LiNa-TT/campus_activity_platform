package com.example.campus.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * ╔══════════════════════════════════════════════════════════════╗
 * ║  模块8 热点新闻推送实体【基础 CRUD + 分类 + 置顶】              ║
 * ╠══════════════════════════════════════════════════════════════╣
 * ║  【五大分类（需求文档硬性要求）】                                ║
 * ║     校园通知 / 教育热点 / 时政新闻 / 竞赛通知 / 就业资讯        ║
 * ║  【五个必填字段（需求文档硬性要求）】                            ║
 * ║     标题(title) / 封面图片(coverUrl) / 正文(content)          ║
 * ║     来源(source) / 发布时间(publishTime)                      ║
 * ║  【特色功能】top 置顶标记：置顶新闻在 /news 列表排最前面        ║
 * ╚══════════════════════════════════════════════════════════════╝
 */
@Entity
@Table(name = "news")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class News {

    /** ⭐主键：自增 ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 新闻标题（必填，需求文档硬性要求 1/5） */
    @Column(nullable = false, length = 120)
    private String title;

    /**
     * 新闻分类
     * 值：校园通知 / 教育热点 / 时政新闻 / 竞赛通知 / 就业资讯
     * 仅 ADMIN 角色可管理本模块（SecurityConfig 配置）
     */
    @Column(length = 50)
    private String category;

    /** 新闻正文（需求文档硬性要求 3/5） */
    @Column(length = 2000)
    private String content;

    /** 新闻封面图片 URL（需求文档硬性要求 2/5） */
    @Column(length = 500)
    private String coverUrl;

    /**
     * ⭐新闻来源（需求文档硬性要求 4/5）
     * 例："人民日报"、"学校教务处"、"教育部官网"
     */
    @Column(length = 100)
    private String source;

    /** 新闻发布时间（需求文档硬性要求 5/5） */
    private LocalDateTime publishTime;

    /**
     * ⭐是否置顶（模块8 特色功能）
     * true 时在新闻列表中排在最前（首页/新闻列表页均生效）
     * 切换接口：POST /admin/news/top/{id}
     */
    private Boolean top = false;
}
