package com.lesson.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("lesson_analysis") // 对应数据库的 AI 解析结果表
public class LessonAnalysis {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long fileId;        // 关联的文件ID（就是因为这个字段，刚才代码才报错）

    private String title;       // 教案标题

    private String gradeSubject;// 年级/学科

    private String objectives;  // 教学目标

    private String keywords;    // 关键词

    private String summary;     // 100字内摘要

    private Long costTime;      // 解析耗时(毫秒)

    private Integer isMock;     // 是否为Mock数据: 1-是, 0-真实API

    private String errorMsg;    // 失败原因

    private LocalDateTime createTime;

}
