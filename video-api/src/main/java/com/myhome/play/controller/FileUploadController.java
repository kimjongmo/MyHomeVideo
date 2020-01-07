package com.myhome.play.controller;

import com.myhome.play.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@CrossOrigin
public class FileUploadController {

    private FileUploadService fileUploadService;

    public FileUploadController(FileUploadService fileUploadService){
        this.fileUploadService = fileUploadService;
    }

    @PostMapping(value = "/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String upload(@RequestParam("file") List<MultipartFile> multipartFiles,
                         @RequestParam("category_id") Long categoryId,
                         @RequestParam List<String> title){
        log.info("category Id : {}, title : {}",categoryId,title.get(0));
        return fileUploadService.upload(multipartFiles,categoryId);
    }

}
