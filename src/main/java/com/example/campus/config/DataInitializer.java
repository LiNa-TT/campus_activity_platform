package com.example.campus.config;

import com.example.campus.entity.*;
import com.example.campus.repository.*;
import com.example.campus.service.ActivityService;
import com.example.campus.service.ConsultationService;
import com.example.campus.service.NewsService;
import com.example.campus.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 初始化数据：六种角色账号 + 覆盖九大模块的示例活动/新闻/咨询 + 订阅推送演示。
 * 全部幂等（仅在表为空时插入）。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;
    private final ActivityService activityService;
    private final NewsService newsService;
    private final ConsultationService consultationService;
    private final SubscriptionService subscriptionService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // 1. 账号
        seedUser("admin", "系统管理员", "ADMIN", "A00001");
        User student = seedUser("student", "张同学", "STUDENT", "20210001");
        seedUser("teacher", "李老师", "TEACHER", "T20001");
        seedUser("clubleader", "王社长", "CLUB_LEADER", "20210002");
        seedUser("eventadmin", "陈赛事", "EVENT_ADMIN", "E30001");
        seedUser("unionadmin", "赵工会", "UNION_ADMIN", "U40001");

        if (activityRepository.count() > 0) return; // 已有数据则跳过内容种子

        // 2. 订阅（演示新活动自动推送）
        subscriptionService.subscribe(student, "讲座");
        subscriptionService.subscribe(student, "社团");
        subscriptionService.subscribe(student, "竞赛");

        // 3. 活动（覆盖模块1/2/3/4/5/7），通过 ActivityService.save 触发分类推送
        save(Activity.builder().title("大学生学业规划讲座").category("讲座").organizer("学生工作处")
                .location("大学生活动中心").startTime(dt(2026, 9, 1, 19, 0)).endTime(dt(2026, 9, 1, 21, 0))
                .maxPeople(300).description("帮助学生制定大学阶段学习与发展规划。").recommended(true)
                .speaker("王教授").department("学生工作处").status("报名中").build());
        save(Activity.builder().title("人工智能前沿学术讲座").category("讲座").organizer("计算机学院")
                .location("图书馆报告厅").startTime(dt(2026, 9, 15, 14, 0)).endTime(dt(2026, 9, 15, 16, 0))
                .maxPeople(200).description("大模型时代的AI发展趋势。").recommended(true)
                .speaker("李院士").department("计算机学院").status("报名中").build());

        save(Activity.builder().title("校园歌手大赛").category("社团").organizer("校团委")
                .location("大学生活动中心").startTime(dt(2026, 9, 5, 18, 30)).endTime(dt(2026, 9, 5, 21, 30))
                .maxPeople(500).description("校园歌手比赛报名与现场活动。").recommended(true)
                .coverUrl("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=campus%20singing%20contest%20poster&image_size=portrait_4_3")
                .status("报名中").build());
        save(Activity.builder().title("动漫社招新活动").category("社团").organizer("动漫社")
                .location("学生活动中心201").startTime(dt(2026, 9, 8, 15, 0)).endTime(dt(2026, 9, 8, 17, 0))
                .maxPeople(80).description("新学期动漫社招新，欢迎同好加入。").status("报名中").build());

        save(Activity.builder().title("蓝桥杯程序设计竞赛").category("竞赛").organizer("信息学院")
                .location("计算机实验楼").startTime(dt(2026, 10, 10, 9, 0)).endTime(dt(2026, 10, 10, 17, 0))
                .maxPeople(150).description("全国软件和信息技术专业人才大赛校内选拔。").recommended(true)
                .requirements("在校本科生，熟悉C/C++/Java/Python任一语言")
                .registrationDeadline(dt(2026, 9, 30, 23, 59))
                .scheduleInfo("初赛：10月10日 09:00-12:00\n决赛：10月10日 14:00-17:00").status("报名中").build());
        save(Activity.builder().title("互联网+大学生创新创业大赛").category("竞赛").organizer("创新创业学院")
                .location("创新创业中心").startTime(dt(2026, 10, 20, 9, 0)).endTime(dt(2026, 10, 20, 18, 0))
                .maxPeople(100).description("双创比赛校级赛。")
                .requirements("需3-5人组队报名，提交项目计划书")
                .registrationDeadline(dt(2026, 10, 15, 23, 59))
                .scheduleInfo("路演答辩：10月20日全天").status("报名中").build());

        save(Activity.builder().title("第38届校运会").category("校运会").organizer("体育部")
                .location("学校体育场").startTime(dt(2026, 10, 25, 8, 0)).endTime(dt(2026, 10, 27, 17, 0))
                .maxPeople(2000).description("校园运动会全流程。").recommended(true)
                .scheduleInfo("10/25 径赛预赛\n10/26 田赛决赛\n10/27 颁奖典礼")
                .resultsInfo("待赛事结束后公示").awardsInfo("待赛事结束后公布").status("报名中").build());

        save(Activity.builder().title("教职工趣味运动会").category("工会").organizer("校工会")
                .location("学校体育场").startTime(dt(2026, 9, 10, 9, 0)).endTime(dt(2026, 9, 10, 12, 0))
                .maxPeople(200).description("面向全校教职工开展趣味运动会。").recommended(true).status("报名中").build());
        save(Activity.builder().title("教师节文艺汇演").category("工会").organizer("校工会")
                .location("大礼堂").startTime(dt(2026, 9, 9, 19, 0)).endTime(dt(2026, 9, 9, 21, 0))
                .maxPeople(300).description("庆祝教师节教职工文艺演出。").status("报名中").build());

        save(Activity.builder().title("教职工羽毛球团体赛").category("教职工文体").organizer("校工会")
                .location("体育馆").startTime(dt(2026, 10, 18, 9, 0)).endTime(dt(2026, 10, 18, 17, 0))
                .maxPeople(120).description("丰富教职工课余文体生活。")
                .scheduleInfo("小组赛 09:00-12:00\n淘汰赛 14:00-17:00").status("报名中").build());

        // 4. 热点新闻（模块8，含来源）
        newsService.save(News.builder().title("2026年秋季校园活动安排发布").category("校园通知")
                .source("校长办公室").publishTime(LocalDateTime.now())
                .content("秋季学期校园活动安排已经发布，请同学们及时关注平台。").top(true).build());
        newsService.save(News.builder().title("教育部公布新一轮双一流建设名单").category("教育热点")
                .source("教育部").publishTime(LocalDateTime.now())
                .content("新一轮双一流建设高校及学科名单正式公布。").build());
        newsService.save(News.builder().title("秋季校园招聘会即将启动").category("就业资讯")
                .source("就业指导中心").publishTime(LocalDateTime.now())
                .content("百家企业进校，提供逾千个岗位。").build());

        // 5. 活动咨询（模块6）
        consultationService.save(Consultation.builder().title("关于活动报名常见问题解答")
                .content("汇总同学们在活动报名过程中遇到的常见问题及解答。").build());
        consultationService.save(Consultation.builder().title("社团活动参与须知")
                .content("社团活动参与须知及注意事项说明。").build());
        consultationService.save(Consultation.builder().title("竞赛报名流程指引")
                .content("详细介绍各类竞赛的报名流程与注意事项。").build());

        log.info("示例数据初始化完成：账号6个，活动9个，新闻3条，咨询2条");
    }

    private Activity save(Activity a) { return activityService.save(a); }

    private LocalDateTime dt(int y, int m, int d, int h, int mi) {
        return LocalDateTime.of(y, m, d, h, mi);
    }

    private User seedUser(String username, String name, String role, String userNo) {
        if (userRepository.existsByUsername(username)) return userRepository.findByUsername(username).orElseThrow();
        return userRepository.save(User.builder()
                .username(username).password(passwordEncoder.encode("123456"))
                .name(name).role(role).userNo(userNo).build());
    }
}
