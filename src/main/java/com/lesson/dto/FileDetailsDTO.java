package com.lesson.dto;

import com.lesson.entity.LessonAnalysis;
import com.lesson.entity.LessonFile;
import com.lesson.entity.LessonFile;
import lombok.Data;

@Data
public class FileDetailsDTO {
    // 文件基础信息
    private LessonFile fileInfo;

    // AI 解析结果（如果还没解析，这里可能为 null）
    private LessonAnalysis analysisResult;
}