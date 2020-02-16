package com.myhome.play.serivce;

import com.myhome.play.exceptions.CategoryNotFoundException;
import com.myhome.play.exceptions.DataSizeNotMatchException;
import com.myhome.play.exceptions.FileDuplicateException;
import com.myhome.play.model.network.Header;
import com.myhome.play.model.network.request.video.VideoInsertRequest;
import com.myhome.play.repo.CategoryRepository;
import com.myhome.play.service.RestTemplateService;
import com.myhome.play.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.*;
import java.net.URI;
import java.util.List;

@Service
@Slf4j
public class FileUploadService {

    @Value("${video.server.ip}")
    private String videoServerIp;

    private CategoryRepository categoryRepository;
    private RestTemplateService restTemplateService;
    private MessageProducerService messageProducerService;
    private FileUtils fileUtils;

    public FileUploadService(CategoryRepository categoryRepository,
                             RestTemplateService restTemplateService,
                             MessageProducerService messageProducerService,
                             FileUtils fileUtils) {
        this.categoryRepository = categoryRepository;
        this.restTemplateService = restTemplateService;
        this.messageProducerService = messageProducerService;
        this.fileUtils = fileUtils;
    }

    public void validate(List<MultipartFile> multipartFiles, Long categoryId, List<String> titles) {
        if (!categoryRepository.findById(categoryId).isPresent())
            throw new CategoryNotFoundException("카테고리를 찾을 수 없습니다");
        if (multipartFiles.size() != titles.size())
            throw new DataSizeNotMatchException("파일에는 제목이 꼭 필요합니다");
    }

    public Header upload(List<MultipartFile> multipartFiles, Long categoryId, List<String> titles) {

        validate(multipartFiles, categoryId, titles);

        String categoryName = categoryRepository.findById(categoryId).get().getName();

        StringBuilder success = new StringBuilder("-----------success list---------------");
        StringBuilder fail = new StringBuilder("-----------fail list ---------------");

        int length = multipartFiles.size();
        for (int i = 0; i < length; i++) {

            MultipartFile file = multipartFiles.get(i);
            String ext = file.getContentType().substring(file.getContentType().lastIndexOf("/") + 1);

            log.info("등록 중... =>file-type : {},name : {}, size : {},ext : {}", file.getContentType(), file.getOriginalFilename(), file.getSize(), ext);

            try {
                fileUpload(file, categoryName);

                //확장자가 avi인 경우 인코딩 서버에 요청
                if (ext.equals("avi")) {
                    boolean isSend = messageProducerService.sendTo(categoryName, file.getOriginalFilename(), titles.get(i));
                    if(!isSend) {
                        fail.append("\n인코딩 서버와 연결이 되지 않았습니다.");
                        continue;
                    }
                    success.append("\n" + file.getOriginalFilename() + "가 등록되었습니다.");
                    continue;
                }

                Header result = insert(makeRequestData(categoryName, titles.get(i), file.getOriginalFilename()));
                if (result.getDescription().equals("SUCCESS")) {
                    success.append("\n" + file.getOriginalFilename() + "가 등록되었습니다.");
                    log.info("--데이터 메타데이터 등록 성공--");
                } else {
                    boolean isDeleted = fileUtils.delete(categoryName, file.getOriginalFilename());
                    fail.append("비디오 서버 연결 오류");
                    log.info("---비디오 메타데이터 등록 실패: {}, 파일 삭제 여부 : {}", result.getDescription(), isDeleted);
                }
            } catch (FileDuplicateException fe) {
                fail.append("\n" + file.getOriginalFilename() + " 파일이 중복됩니다");
            } catch (IOException e) {
                e.printStackTrace();
                fail.append("\n" + file.getOriginalFilename());
            }
        }
        String ret = success.toString() + "\n" + fail.toString();
        return Header.MESSAGE(ret);
    }


    /**
     * HOME_PATH + path 경로에 파일을 저장한다.
     *
     * @param multipartFile 저장할 파일
     * @param category 카테고리명
     * @throws FileNotFoundException
     */
    public void fileUpload(MultipartFile multipartFile, String category) throws IOException {

        boolean isCreated = fileUtils.create(category, multipartFile.getOriginalFilename());

        //기존에 존재하는 파일인 경우 예외를 던진다
        if (!isCreated)
            throw new FileDuplicateException();

        //파일 생성
        File uploadFile = fileUtils.getFile(category, multipartFile.getOriginalFilename());

        //데이터 복사
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

    /**
     * VideoInsertRequest 객체를 만든다.
     *
     * @param categoryName 카테고리 이름
     * @param title        비디오 제목
     * @param fileName     파일 이름 (확장자가 포함되어야 한다)
     * @return 인자값을 이용하여 만들어진 VideoInsertRequest 객체
     */
    private VideoInsertRequest makeRequestData(String categoryName, String title, String fileName) {
        VideoInsertRequest requestData = new VideoInsertRequest();
        requestData.setCategoryName(categoryName);
        requestData.setTitle(title);
        requestData.setFileName(fileName);
        return requestData;
    }

    /**
     * 비디오 메타 정보를 VIDEO_API 서버에 전송
     */
    public Header insert(VideoInsertRequest request) {
        URI uri = UriComponentsBuilder
                .fromHttpUrl(videoServerIp)
                .path("/video")
                .build()
                .toUri();

        ParameterizedTypeReference<Header> type = new ParameterizedTypeReference<Header>() {
        };

        try {
            return restTemplateService.exchange(uri, HttpMethod.POST, request, type);
        } catch (ResourceAccessException e) {
            return Header.ERROR("VIDEO 서버와 연결이 되지 않습니다.");
        } catch (Exception ex) {
            log.info("[FileUploadService] error = {}", ex);
            return Header.ERROR("알 수 없는 오류...");
        }
    }
}
