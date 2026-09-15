# 校园活动聚合平台

## 一、对应题目要求
本项目按照题目图片中的“高校活动聚合平台”设计，实现：
1. 讲座模块：讲座主题、主讲人、时间地点、院系、在线报名。
2. 社团活动模块：招新、团建、演出、培训等活动发布与报名。
3. 校园运动会模块：赛事通知、项目安排、参赛报名、成绩信息入口。
4. 校运会/赛事信息管理模块：赛事信息发布、项目安排、参赛管理。
5. 工会模块：教职工活动发布、节日/团建/培训活动报名。
6. 活动咨询管理模块：活动资讯、活动管理。
7. 校园教职工大型文体活动模块：赛事通知、组织报名、赛事安排、获奖公告。
8. 热点新闻推送模块：新闻增删改、置顶、分类、标题/封面/正文/来源/发布时间等字段。

## 二、技术栈
- Java 17
- Spring Boot 3.5
- Spring MVC
- Spring Data JPA
- Thymeleaf
- MySQL 8
- Maven

## 三、运行
1. 安装 JDK 17、Maven、MySQL 8。
2. 创建数据库并执行 `src/main/resources/data.sql` 中的 SQL（如果使用 `ddl-auto: update`，表会自动创建）。
3. 修改 `application.yml` 中的 MySQL 用户名和密码。
4. 在项目根目录执行：
   `mvn clean package`
5. 运行：
   `java -jar target/campus-activity-platform-1.0.0.jar`
6. 浏览器访问：
   `http://localhost:8080/`

## 四、数据库
数据库名：`campus_activity`

建议先执行：
```sql
CREATE DATABASE campus_activity DEFAULT CHARACTER SET utf8mb4;
```

## 五、提交材料
可以直接将本项目源码作为“项目源码”基础，再补充：
- 项目演示 PPT
- 实验报告
- 项目部署截图
- 数据库运行截图
- 功能测试截图

## 六、说明
这是课程项目的可运行基础版本，重点覆盖图片中列出的八类业务模块。若老师要求严格按照指定前端框架、权限系统、文件上传、成绩管理等细项，可在此基础上继续扩展。
