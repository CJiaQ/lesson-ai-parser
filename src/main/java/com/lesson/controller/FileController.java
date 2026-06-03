package com.lesson.controller;

import com.lesson.common.Result;
import com.lesson.dto.FileDetailsDTO;
import com.lesson.service.FileService;
import com.lesson.service.impl.FileServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
@CrossOrigin
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public Result<String> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("uploader") String uploader) {

        return fileService.upload(file, uploader);
    }
    @GetMapping("/list")
    public Result<?>list(){
        return  fileService.list();
    }
    @GetMapping("/{id}")
    public Result<FileDetailsDTO>getFileDetails(@PathVariable("id") Long id){
        return fileService.getDetails(id);
    }
    @PostMapping("/analyze/{id}")
    public Result<String>analyzeFile(@PathVariable("id") Long id){
        return fileService.analyze(id);
    }
}