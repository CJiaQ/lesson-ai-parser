## 项目简介
基于Spring Boot + MySQL + MyBatis Plus实现的教案文件管理系统

## 环境要求
Java 17 + Maven 3.9+ +MySQL 8.0

# 项目启动
## 1.创建数据库

```sql
CREATE DATABASE lesson_ai DEFAULT CHARSET utf8mb4;
USE lesson_ai;
```

## 2.执行建表SQL

```sql
CREATE TABLE `lesson_file` (
`id` bigint(20) NOT NULL AUTO_INCREMENT,
`file_name` varchar(20) NOT NULL COMMENT '文件名',
`file_type` varchar(20) NOT NULL COMMENT '文件类型',
`file_size` bigint(20) NOT NULL COMMENT '文件大小(byte)',
`file_path` varchar(500) NOT NULL COMMENT '文件存储路径',
`uploader` varchar(100) NOT NULL COMMENT '上传人',
`upload_time` datetime NOT NULL COMMENT '上传时间',
`status` varchar(20) NOT NULL COMMENT '状态',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `lesson_analysis` (
`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
`file_id` bigint(20) NOT NULL COMMENT '关联的文件ID',
`title` varchar(255) DEFAULT NULL COMMENT '教案标题',
`grade_subject` varchar(100) DEFAULT NULL COMMENT '年级/学科',
`objectives` text COMMENT '教学目标',
`keywords` varchar(255) DEFAULT NULL COMMENT '关键词',
`summary` text COMMENT '摘要',
`cost_time` bigint(20) DEFAULT NULL COMMENT '解析耗时(毫秒)',
`is_mock` tinyint(4) DEFAULT '1' COMMENT '是否为Mock数据: 1-是, 0-真实API',
`error_msg` text COMMENT '失败原因',
`create_time` datetime DEFAULT NULL COMMENT '创建时间',
PRIMARY KEY (`id`),
KEY `idx_file_id` (`file_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='AI教案解析结果表';
```

## 3.修改数据库配置

```md
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/lesson_ai?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver

file:
  upload-path: ./uploads/
```

## 4.构建项目

```md
mvn clean package
```
## 5.运行项目
在项目根目录执行：
```md
java -jar target/lesson-ai-parser-0.0.1-SNAPSHOT.jar
```
<img width="1775" height="200" alt="qidong" src="https://github.com/user-attachments/assets/f57895ef-d4d0-4cd1-afb8-84d8ba45d65f" />

启动成功后访问：
```md
http://localhost:8080
```

# 文件上传接口 
## 接口地址
```md
POST /api/files/upload
```

## 请求参数
| 参数名 | 类型 | 必填 |
|--------|------|------|
| file | File | 是 |
| uploader | Text | 是 |

## 测试截图：
<img width="1238" height="673" alt="docup" src="https://github.com/user-attachments/assets/ca795342-2ebe-47e5-8947-87d6cf0ef70e" />

## 返回结果：
```json
{ 
   "code":200, 
   "message":"success", 
   "data":"上传成功" 
}
```
## 数据库验证
执行SQL:
```sql
 SELECT * FROM lesson_file WHERE uploader='test';
```
<img width="1295" height="283" alt="docup_mysql" src="https://github.com/user-attachments/assets/221e1f99-4f8f-4ce6-8861-cb37b4435a0e" />

查询结果：

| id | file_name | file_type | file_size | file_path | uploader | upload_time | status |
|----|-----------|-----------|-----------|-----------|----------|-------------|--------|
| 5 | learn.pdf | pdf | 405237 | C:\Users\26235\Downloads\lesson-ai-parser\lession-ai-parser\.\uploads\4cfb6ed5-0eab-4ba3-822d-be46c69b5423.pdf | test | 2026-06-06 00:01:59 | UPLOADED |

## 验证说明
返回结果包含刚刚上传的 learn.pdf 记录，与数据库内容一致。

# 文件列表查询接口
## 接口地址:
```md
GET /api/files/list
```

## 测试截图:

<img width="1651" height="1034" alt="doclist" src="https://github.com/user-attachments/assets/fd9d9515-03da-488d-ab5d-2ce9908fe3ee" />

## 返回结果:
```json
{
    "code": 200,
    "message": "success",
    "data": [
        {
            "id": 1,
            "fileName": "CET6成绩单.pdf",
            "fileType": "pdf",
            "fileSize": 425251,
            "filePath": "C:\\Users\\26235\\Downloads\\lession-ai-parser\\lession-ai-parser\\.\\uploads\\e513786a-d47a-4528-9456-bec12115ff9b.pdf",
            "uploader": "admin",
            "uploadTime": "2026-06-03T22:54:43",
            "status": "SUCCESS"
        },
        {
            "id": 2,
            "fileName": "蔡佳琪_山西大学_后端开发.pdf",
            "fileType": "pdf",
            "fileSize": 858206,
            "filePath": "C:\\Users\\26235\\Downloads\\lesson-ai-parser\\lession-ai-parser\\.\\uploads\\fc2fbe26-1e68-40b4-bf58-d4b000a73e0b.pdf",
            "uploader": "admin",
            "uploadTime": "2026-06-04T12:37:30",
            "status": "UPLOADED"
        },
        {
            "id": 3,
            "fileName": "蔡佳琪_山西大学_后端开发.pdf",
            "fileType": "pdf",
            "fileSize": 858206,
            "filePath": "C:\\Users\\26235\\Downloads\\lesson-ai-parser\\lession-ai-parser\\.\\uploads\\b9765d55-e035-4512-a1c4-75d6ca3b6585.pdf",
            "uploader": "admin",
            "uploadTime": "2026-06-05T23:56:42",
            "status": "UPLOADED"
        },
        {
            "id": 4,
            "fileName": "蔡佳琪_山西大学_后端开发.pdf",
            "fileType": "pdf",
            "fileSize": 858206,
            "filePath": "C:\\Users\\26235\\Downloads\\lesson-ai-parser\\lession-ai-parser\\.\\uploads\\2eaaa784-c3b2-467a-ae75-6b9db6175aa5.pdf",
            "uploader": "newtest",
            "uploadTime": "2026-06-06T00:00:25",
            "status": "UPLOADED"
        },
        {
            "id": 5,
            "fileName": "learn.pdf",
            "fileType": "pdf",
            "fileSize": 405237,
            "filePath": "C:\\Users\\26235\\Downloads\\lesson-ai-parser\\lession-ai-parser\\.\\uploads\\4cfb6ed5-0eab-4ba3-822d-be46c69b5423.pdf",
            "uploader": "test",
            "uploadTime": "2026-06-06T00:01:59",
            "status": "UPLOADED"
        }
    ]
}
```
## 数据库验证
执行SQL:
```sql
 select *from lesson_file;
```

查询结果：

<img width="1895" height="351" alt="l" src="https://github.com/user-attachments/assets/4cf24b06-aa11-4c06-8198-388eb7c0b65a" />

## 验证说明
返回记录与 lesson_file 表对应 id 的数据一致。

# 文件详情接口
## 接口地址：
```md
GET /api/files/5
```

## 测试截图：

<img width="1254" height="908" alt="doclist" src="https://github.com/user-attachments/assets/98f2167b-3600-43cb-8781-5ab471c5af5d" />

## 返回结果：
```json
{
    "code": 200,
    "message": "success",
    "data": {
        "fileInfo": {
            "id": 5,
            "fileName": "learn.pdf",
            "fileType": "pdf",
            "fileSize": 405237,
            "filePath": "C:\\Users\\26235\\Downloads\\lesson-ai-parser\\lession-ai-parser\\.\\uploads\\4cfb6ed5-0eab-4ba3-822d-be46c69b5423.pdf",
            "uploader": "test",
            "uploadTime": "2026-06-06T00:01:59",
            "status": "UPLOADED"
        },
        "analysisResult": null
    }
}
```
## 数据库验证
执行SQL:
```sql
 select *from lesson_file where id=5;
```
查询结果：

<img width="1784" height="134" alt="docupsql" src="https://github.com/user-attachments/assets/4ca24e6f-be3a-403a-b867-00e571fccf8a" />

## 验证说明

返回记录与 lesson_file 表对应 id 的数据一致。

# AI解析接口（MOCK版本）
## 接口地址：
```md
POST /api/files/parse/5
```

## 测试截图：
<img width="1260" height="700" alt="image" src="https://github.com/user-attachments/assets/6dabde1a-5fcd-4382-94ee-e1764281debb" />

## 返回结果：
```json
{
    "code": 200,
    "message": "success",
    "data": "解析任务执行成功"
}
```

## 数据库验证：
```sql
 SELECT * FROM lesson_analysis;
```

查询结果：

<img width="1706" height="510" alt="aisql" src="https://github.com/user-attachments/assets/8097736a-babd-436e-bbbe-bfc2a0f06436" />

| id | file_id | title | grade_subject | objectives | keywords | summary | cost_time | is_mock | error_msg | create_time |
|----|---------|-------|---------------|------------|----------|---------|-----------|---------|-----------|-------------|
| 1 | 1 | 高一英语 Unit 1 Making Friends 优秀教案设计 | 高一 / 英语 | 1. 掌握本单元核心词汇及表达方式;<br>2. 能够熟练运用情景对话与他人建立社交联系;<br>3. 培养学生跨文化交际的自信心与同理心。 | Making Friends, 核心词汇, 情景对话, 跨文化交际 | 本教案围绕高一英语'广交朋友'主题展开，通过引入互动情景和口语交际任务，在全英文沉浸式的课堂设计中，引导学生迅速建立语感，重点突破社交场景下的口语表达障碍。 | 1519 | 1 | NULL | 2026-06-04 00:24:04 |
| 2 | 5 | 高一英语 Unit 1 Making Friends 优秀教案设计 | 高一 / 英语 | 1. 掌握本单元核心词汇及表达方式;<br>2. 能够熟练运用情景对话与他人建立社交联系;<br>3. 培养学生跨文化交际的自信心与同理心。 | Making Friends, 核心词汇, 情景对话, 跨文化交际 | 本教案围绕高一英语'广交朋友'主题展开，通过引入互动情景和口语交际任务，在全英文沉浸式的课堂设计中，引导学生迅速建立语感，重点突破社交场景下的口语表达障碍。 | 1512 | 1 | NULL | 2026-06-06 00:55:22 |

## 验证说明：
解析任务执行后生成了解析结果，并保存到了数据库中。

## AI辅助内容

本项目开发过程中使用了 ChatGPT 与 Gemini 作为辅助开发工具。

### 1. 项目结构设计

使用 AI 辅助生成 Spring Boot 项目基础结构，包括：

- Entity
- Mapper
- Service
- ServiceImpl
- Controller
- Result统一返回对象

根据项目需求进行调整。

### 2. 接口代码生成

使用 AI 辅助生成以下接口的基础代码：

- 文件上传接口
- 文件列表接口
- 文件详情接口
- AI解析任务接口
- 解析结果查询接口

生成后结合项目实际情况进行了修改和调试。

### 3. 数据库设计辅助

使用 AI 辅助设计数据库表结构：

- lesson_file（文件信息表）
- lesson_parse_result（解析结果表）

### 4. Mock AI解析逻辑

AI辅助生成了模拟解析逻辑，用于生成：

- 教案标题
- 年级学科
- 教学目标
- 关键词
- 教案摘要

并将结果保存至数据库中。


## 开发者工作
对AI生成代码进行检查和修改：

1. 修复MyBatis Plus配置问题，解决项目启动失败问题。

2. 修复Result返回对象问题，解决接口返回数据为null的问题。

3. 修复文件上传路径问题，解决上传文件时报 FileNotFoundException 的问题。

4. 调整application.yml配置，补充文件上传目录配置。

5. 检查数据库表结构与实体类映射关系，确保字段正确保存。

6. 对上传、列表、详情、解析任务、解析结果查询接口进行联调测试。

7. 补充异常处理与参数校验逻辑。

8. 编写并完善README、自测说明与接口文档。

