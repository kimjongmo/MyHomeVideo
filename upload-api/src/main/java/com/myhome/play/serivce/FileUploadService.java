package com.myhome.play.serivce;

import com.myhome.play.exceptions.CategoryNotFoundException;
import com.myhome.play.exceptions.DataSizeNotMatchException;
import com.myhome.play.exceptions.FileDuplicateException;
import com.myhome.play.model.network.Header;
import com.myhome.play.model.network.request.video.VideoInsertRequest;
import com.myhome.play.repo.CategoryRepository;
import com.myhome.play.service.RestTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.*;
import java.net.URI;
import java.util.List;

@Service
@Slf4j
public class FileUploadService {

    @Value("${home.path}")
    public String ROOT_PATH;
    @Value("${video.server.ip}")
    private String videoServerIp;

    private CategoryRepository categoryRepository;
    private RestTemplateService restTemplateService;

    public FileUploadService(CategoryRepository categoryRepository,RestTemplateService restTemplateService) {
        this.categoryRepository = categoryRepository;
        this.restTemplateService = restTemplateService;
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

                Header result = insert(getRequestData(categoryName, titles.get(i), file.getOriginalFilename()));

                if(result.getDescription().equals("SUCCESS")) {
                    success.append("\n" + file.getOriginalFilename()+"가 등록되었습니다.");
                    log.info("--데이터 메타데이터 등록 성공--");
                }
               else{
                   // TODO: 2020-02-03 파일 삭제
                   log.info("---비디오 메타데이터 등록 실패:"+result.getDescription());
               }
            }catch(FileDuplicateException fe){
              fail.append("\n"+file.getOriginalFilename()+" 파일이 중복됩니다");
            } catch (IOException e) {
                e.printStackTrace();
                fail.append("\n" + file.getOriginalFilename());
            }
        }
        String ret = success.toString() + "\n" + fail.toString();
        return Header.MESSAGE(ret);
    }

    public void fileUpload(MultipartFile multipartFile, String path) throws IOException {
        File uploadFile = new File(path + "/" + multipartFile.getOriginalFilename());
        if(uploadFile.exists()){
            throw new FileDuplicateException();
        }
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

    public Header insert(VideoInsertRequest request){
        URI uri = UriComponentsBuilder
                .fromHttpUrl(videoServerIp)
                .path("/video")
                .build()
                .toUri();

        ParameterizedTypeReference<Header> type = new ParameterizedTypeReference<Header>() {};

        return restTemplateService.exchange(uri, HttpMethod.POST,request,type);
    }
}
