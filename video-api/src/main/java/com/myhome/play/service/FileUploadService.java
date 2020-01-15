package com.myhome.play.service;

import com.myhome.play.exceptions.CategoryNotFoundException;
import com.myhome.play.exceptions.DataSizeNotMatchException;
import com.myhome.play.model.entity.Category;
import com.myhome.play.model.network.Header;
import com.myhome.play.repo.CategoryRepository;
import com.myhome.play.repo.VideoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FileUploadService {

    @Value("${home.path}")
    public String ROOT_PATH;

    private CategoryRepository categoryRepository;
    private VideoRepository videoRepository;

    public FileUploadService(CategoryRepository categoryRepository, VideoRepository videoRepository) {
        this.categoryRepository = categoryRepository;
        this.videoRepository = videoRepository;
    }

    public void validate(List<MultipartFile> multipartFiles, Long categoryId, List<String> titles) {

        if (!categoryRepository.findById(categoryId).isPresent())
            throw new CategoryNotFoundException("카테고리를 찾을 수 없습니다");

        if (multipartFiles.size() != titles.size()) {
            throw new DataSizeNotMatchException("파일에는 제목이 꼭 필요합니다");
        }

    }

    public Header upload(List<MultipartFile> multipartFiles, Long categoryId, List<String> titles) {

        validate(multipartFiles, categoryId, titles);

        String path = ROOT_PATH + "/" + categoryRepository.findById(categoryId).get().getName();

        StringBuilder success = new StringBuilder("-----------upload success list ---------------");
        StringBuilder fail = new StringBuilder("--------------upload fail list------------------");

        multipartFiles.forEach(file -> {
            log.info("{}, {}, {}", file.getContentType(), file.getOriginalFilename(), file.getSize());
            try {
                fileUpload(file, path);
                success.append("\n" + file.getOriginalFilename());
            } catch (IOException e) {
                e.printStackTrace();
                fail.append("\n" + file.getOriginalFilename());
            }
        });
        String ret = success.toString() + "\n" + fail.toString();
        return Header.OK(ret);
    }

    private void fileUpload(MultipartFile multipartFile, String path) throws IOException {
        File uploadFile = new File(path + "/" + multipartFile.getOriginalFilename());
        uploadFile.createNewFile();
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(uploadFile));
             BufferedInputStream bis = new BufferedInputStream(multipartFile.getInputStream());) {
            byte[] buffer = new byte[4096 * 2];
            int read = 0;
            while ((read = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, buffer.length);
            }
            bos.flush();
        } catch (Exception e) {
            log.info("{}", e);
        }
    }
}
