package com.lesson.service.impl;

import com.baomidou.mybatisplus.core.assist.ISqlRunner;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lesson.common.Result;
import com.lesson.dto.FileDetailsDTO;
import com.lesson.entity.LessonAnalysis;
import com.lesson.entity.LessonFile;
import com.lesson.mapper.LessonAnalysisMapper;
import com.lesson.mapper.LessonFileMapper;
import com.lesson.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private final LessonFileMapper lessonFileMapper;
    private final LessonAnalysisMapper lessonAnalysisMapper;
    public FileServiceImpl(LessonFileMapper lessonFileMapper,LessonAnalysisMapper lessonAnalysisMapper) {
        this.lessonFileMapper = lessonFileMapper;
        this.lessonAnalysisMapper=lessonAnalysisMapper;
    }

    @Value("${file.upload-path}")
    private String uploadPath;

    @Override
    public Result<String> upload(MultipartFile file, String uploader) {

        if (file == null || file.isEmpty()) {
            return Result.fail("文件不能为空");
        }

        String fileName = file.getOriginalFilename();

        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

        if (!suffix.equals("pdf")
                && !suffix.equals("doc")
                && !suffix.equals("docx")) {
            return Result.fail("仅支持PDF、DOC、DOCX");
        }

        if (file.getSize() > 50 * 1024 * 1024) {
            return Result.fail("文件超过50MB");
        }

        try {

            String projectPath = System.getProperty("user.dir");

            File dir = new File(projectPath, uploadPath);

            if (!dir.exists()) {
                dir.mkdirs();
            }

            String saveName =
                    UUID.randomUUID() + "." + suffix;

            File targetFile = new File(dir, saveName);

            System.out.println("目录=" + dir.getAbsolutePath());
            System.out.println("文件=" + targetFile.getAbsolutePath());

            file.transferTo(targetFile);

            LessonFile lessonFile = new LessonFile();

            lessonFile.setFileName(fileName);
            lessonFile.setFileType(suffix);
            lessonFile.setFileSize(file.getSize());
            lessonFile.setFilePath(targetFile.getAbsolutePath());
            lessonFile.setUploader(uploader);
            lessonFile.setUploadTime(LocalDateTime.now());
            lessonFile.setStatus("UPLOADED");

            lessonFileMapper.insert(lessonFile);

            return Result.success("上传成功");

        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }
    @Override
    public Result<?>list(){
        return Result.success(
                lessonFileMapper.selectList(null)
        );
    }
    @Override
    public Result<FileDetailsDTO> getDetails(Long id) {
        // 1. 查询文件基础信息（明确用文件自己的 mapper）
        LessonFile file = this.lessonFileMapper.selectById(id);
        if (file == null) {
            return Result.fail("未找到该文件信息");
        }

        // 2. 严格指定泛型为 LessonAnalysis，且由实例对象 lessonAnalysisMapper 来调用非静态方法
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<LessonAnalysis> queryWrapper =
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();

        queryWrapper.eq(LessonAnalysis::getFileId, id);

        LessonAnalysis analysis = this.lessonAnalysisMapper.selectOne(queryWrapper);

        // 3. 组装 DTO
        FileDetailsDTO dto = new FileDetailsDTO();
        dto.setFileInfo(file);
        dto.setAnalysisResult(analysis);

        return Result.success(dto);
    }
    @Override
    public Result<String> analyze(Long fileId) {
        // 1. 检查文件是否存在
        LessonFile file = lessonFileMapper.selectById(fileId);
        if (file == null) {
            return Result.fail("文件不存在");
        }

        // 2. 检查是否已经解析过，防止重复解析
        LambdaQueryWrapper<LessonAnalysis> queryWrapper =
                new LambdaQueryWrapper<>();
        queryWrapper.eq(LessonAnalysis::getFileId, fileId);
        LessonAnalysis existAnalysis = lessonAnalysisMapper.selectOne(queryWrapper);
        if (existAnalysis != null) {
            return Result.fail("该文件已存在解析结果，请勿重复解析");
        }

        long startTime = System.currentTimeMillis();

        try {
            // 3. 模拟“解析中”的状态更新
            file.setStatus("ANALYZING");
            lessonFileMapper.updateById(file);

            // 4. 调用解析核心方法（这里可以通过修改开关随时切换 Mock 或 真实 API）
            boolean useMock = true; // 🌟 想换成真实 DeepSeek API 时，把这里改成 false 即可

            LessonAnalysis result;
            if (useMock) {
                result = executeMockAnalysis(fileId);
            } else {
                result = executeDeepSeekAnalysis(fileId, file.getFileName());
            }

            // 5. 计算并补全耗时等元数据
            long costTime = System.currentTimeMillis() - startTime;
            result.setCostTime(costTime);
            result.setCreateTime(LocalDateTime.now());

            // 6. 保存解析结果，更新文件状态
            lessonAnalysisMapper.insert(result);

            file.setStatus("SUCCESS");
            lessonFileMapper.updateById(file);

            return Result.success("解析任务执行成功");

        } catch (Exception e) {
            // 7. 异常处理：万一失败了，记录失败原因和状态
            long costTime = System.currentTimeMillis() - startTime;
            LessonAnalysis errorResult = new LessonAnalysis();
            errorResult.setFileId(fileId);
            errorResult.setIsMock(1);
            errorResult.setCostTime(costTime);
            errorResult.setErrorMsg(e.getMessage());
            errorResult.setCreateTime(LocalDateTime.now());
            lessonAnalysisMapper.insert(errorResult);

            file.setStatus("FAILED");
            lessonFileMapper.updateById(file);

            return Result.fail("解析失败：" + e.getMessage());
        }
    }

    /**
     * 方案 A：Mock 模拟数据返回（只要 1 个参数）
     */
    private LessonAnalysis executeMockAnalysis(Long fileId) throws InterruptedException {
        // 模拟网络延迟
        Thread.sleep(1500);

        LessonAnalysis analysis = new LessonAnalysis();
        analysis.setFileId(fileId);
        analysis.setTitle("高一英语 Unit 1 Making Friends 优秀教案设计");
        analysis.setGradeSubject("高一 / 英语");
        analysis.setObjectives("1. 掌握本单元核心词汇及表达方式;\n2. 能够熟练运用情景对话与他人建立社交联系;\n3. 培养学生跨文化交际的自信心与同理心。");
        analysis.setKeywords("Making Friends, 核心词汇, 情景对话, 跨文化交际");
        analysis.setSummary("本教案围绕高一英语‘广交朋友’主题展开，通过引入互动情景和口语交际任务，在全英文沉浸式的课堂设计中，引导学生迅速建立语感，重点突破社交场景下的口语表达障碍。");
        analysis.setIsMock(1);
        return analysis;
    }

    /**
     * 方案 B：对接真实 DeepSeek 大模型（需要 2 个参数，留空防报错）
     */
    private LessonAnalysis executeDeepSeekAnalysis(Long fileId, String fileName) {
        throw new RuntimeException("DeepSeek API 尚未配置，Mock 模式下自测！");
    }

}