package com.lesson.service;

import com.lesson.common.FileStatus;
import com.lesson.common.Result;
import com.lesson.dto.FileDetailsDTO;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    Result<String> upload(MultipartFile file,String uploader);
    Result<?>list();
    Result<FileDetailsDTO>getDetails(Long id);
    Result<String>analyze(Long fileId);
}