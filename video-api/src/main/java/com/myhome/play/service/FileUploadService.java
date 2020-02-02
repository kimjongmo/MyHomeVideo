package com.myhome.play.service;

import com.myhome.play.exceptions.CategoryNotFoundException;
import com.myhome.play.exceptions.DataSizeNotMatchException;
import com.myhome.play.model.entity.Category;
import com.myhome.play.model.network.Header;
import com.myhome.play.model.network.request.video.VideoInsertRequest;
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
    private VideoApiService videoApiService;

    public FileUploadService(CategoryRepository categoryRepository, VideoRepository videoRepository, VideoApiService videoApiService) {
        this.categoryRepository = categoryRepository;
        this.videoRepository = videoRepository;
        this.videoApiService = videoApiService;
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
        String categoryName = categoryRepository.findById(categoryId).get().getName();
        String path = ROOT_PATH + "/" + categoryName;

        StringBuilder success = new StringBuilder("-----------upload success list ---------------");
        StringBuilder fail = new StringBuilder("--------------upload fail list------------------");

        int length = multipartFiles.size();
        for (int i = 0; i < length; i++) {
            MultipartFile file = multipartFiles.get(i);
            log.info("등록 중... =>file-type : {},name : {}, size : {}", file.getContentType(), file.getOriginalFilename(), file.getSize());
            try {
                fileUpload(file, path);
                success.append("\n" + file.getOriginalFilename());
                // 데이터 등록
                videoApiService.insert(getRequestData(categoryName, titles.get(i), file.getOriginalFilename()));
            } catch (IOException e) {
                e.printStackTrace();
                fail.append("\n" + file.getOriginalFilename());
                // TODO: 2020-01-25 파일은 업로드 됬는데 insert()에서 실패시는 어떻게 할지 
            }
        }
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

    private VideoInsertRequest getRequestData(String categoryName, String title, String fileName) {
        VideoInsertRequest requestData = new VideoInsertRequest();
        requestData.setCategoryName(categoryName);
        requestData.setTitle(title);
        requestData.setFileName(fileName);
        return requestData;
    }
}
