## 项目简介
基于Spring Boot + MySQL + MyBatis Plus实现的教案文件管理系统

## 技术栈
Java 17 + Spring Boot + MyBatis Plus + MySQL
Maven + Lombok

## 建表SQL
--1.教案文件基础信息表

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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4

--2.AI结构化解析结果表

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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='AI教案解析结果表'

## 接口说明
1. 文件上传接口
接口地址:
POST /api/files/upload
功能说明:
上传教案文件，仅支持 PDF、DOC、DOCX 格式，文件大小不超过 50MB。
---

2. 文件列表查询接口
接口地址:

GET /api/files/list

功能说明

查询所有已上传文件。


---

3. 文件详情接口

接口地址

GET /api/files/{id}

功能说明

根据文件 ID 查询文件详细信息。


---

4. AI解析接口

接口地址

POST /api/files/parse/{id}

功能说明

对已上传教案进行 AI 解析，生成标题、年级、学科、教学目标、关键词及摘要。


## 自测说明
文件上传接口测试
![docup.png](../../../Pictures/%E6%88%AA%E5%9B%BE/docup.png)
文件列表测试
![doclist.png](../../../Pictures/%E6%88%AA%E5%9B%BE/doclist.png)
文件详情测试
![docdl.png](../../../Pictures/%E6%88%AA%E5%9B%BE/docdl.png)
AI解析测试
![docanaly.png](../../../Pictures/%E6%88%AA%E5%9B%BE/docanaly.png)

## AI工具使用
开发过程中使用了Gemini和ChatGPT作为辅助工具。

-根据需求生成项目初始结构

-生成Controller,Service,Mapper基础代码

-生成文件上传,查询等接口示例代码
## 开发者工作
-项目搭建

-Spring Boot配置

-MyBatis Plus集成

-文件上传功能调试

-文件路径问题排查与修复

-接口联调与测试
