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