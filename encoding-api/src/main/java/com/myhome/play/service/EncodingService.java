package com.myhome.play.service;

import com.myhome.play.domain.EncodingHistory;
import com.myhome.play.enums.EncodingResult;
import com.myhome.play.model.network.Header;
import com.myhome.play.model.network.request.encode.EncodeRequestDTO;
import com.myhome.play.model.network.request.video.VideoInsertRequest;
import com.myhome.play.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.*;
import java.net.URI;
import java.time.LocalDateTime;

@Service
@Slf4j
public class EncodingService {

    @Value("${ffmpeg.path}")
    public String FFMPEG_PATH;

    @Value("${video.server.ip}")
    public String videoServerIp;

    private RestTemplateService restTemplateService;
    private EncodingHistoryService encodingHistoryService;
    private FileUtils fileUtils;

    public EncodingService(RestTemplateService restTemplateService,
                           EncodingHistoryService encodingHistoryService,
                           FileUtils fileUtils) {
        this.encodingHistoryService = encodingHistoryService;
        this.restTemplateService = restTemplateService;
        this.fileUtils = fileUtils;
    }

    /**
     * 비디오 파일을 MPEG-4 타입으로 인코딩한다.
     *
     * @date 2020.02.09
     * @author kimjongmo
     */
    public EncodingResult encodingToMPEG4(EncodeRequestDTO requestDTO) {
        //Validate Input
        if (!validate(requestDTO)) return EncodingResult.INPUT_ERROR;

        File file = fileUtils.getFile(requestDTO.getCategory(), requestDTO.getName());

        //file is not existed
        if (!file.exists()) return EncodingResult.INPUT_ERROR;

        //인코딩 히스토리
        EncodingHistory history = EncodingHistory.builder()
                .fileName(file.getName())
                .fileSize(Math.round((file.length() / 1000000.0) * 10) / 10.0 + "MB")
                .startAt(LocalDateTime.now())
                .build();

        String sourcePath = file.getAbsolutePath();//인코딩할 파일의 절대경로
        String targetPath = fileUtils.getPureName(file.getAbsolutePath()) + ".mp4"; //인코딩 된 파일의 목적 경로

        //비디오 인코딩
        boolean isSuccess = encodingVideo(sourcePath, targetPath);


        if (!isSuccess) {
            historySave(history, EncodingResult.ERROR);
            return EncodingResult.ERROR;
        }

        // TODO: 2020-02-13 file.getName()하면 avi 확장자 명이 나옴. 고칠 것. 
        // VideoInsertRequest 객체를 생성 후 video 서버에 전달.
        Header header
                = insert(makeRequestData(requestDTO.getCategory(), requestDTO.getTitle(), file.getName()));

        //avi 파일 삭제
        fileUtils.delete(requestDTO.getCategory(), requestDTO.getName());

        if (header.getDescription().equals("SUCCESS")) {
            historySave(history, EncodingResult.OK);
            return EncodingResult.OK;
        }

        //메타데이터 등록 실패 시
        fileUtils.delete(requestDTO.getCategory(), file.getName());
        historySave(history, EncodingResult.SAVE_META_DATA_FAIL);
        return EncodingResult.SAVE_META_DATA_FAIL;
    }

    public EncodingHistory historySave(EncodingHistory history, EncodingResult result) {
        history.setEndAt(LocalDateTime.now());
        history.setEncodingResult(result);
        return encodingHistoryService.save(history);
    }

    /**
     * validate null and empty
     *
     * @param dto EncodeRequestDTO
     * @return <code>true</code> null or empty; <code>false</code> validate.
     */
    public boolean validate(EncodeRequestDTO dto) {
        //값 존재 유무
        if (Strings.isEmpty(dto.getCategory()) || Strings.isEmpty(dto.getName()) || Strings.isEmpty(dto.getTitle()))
            return false;

        // TODO: 2020-02-12 확장자 검사 
        return true;
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

        int retry = 3;
        while (retry-- > 0) {
            try {
                Header header = restTemplateService.exchange(uri, HttpMethod.POST, request, type);
                return header;
            } catch (ResourceAccessException e) {
                log.error("VIDEO 서버 연결 실패...");
            } catch (Exception ex) {
                log.info("[FileUploadService] error = {}", ex);
                return Header.ERROR("알 수 없는 오류...");
            }
        }
        return Header.ERROR("VIDEO 서버와 연결이 되지 않습니다.");

    }

    /**
     * 동영상을 인코딩한다.
     */
    public boolean encodingVideo(String sourcePath, String targetPath) {
        //ffmpeg 프로그램의 옵션 설정
        String[] commands = {FFMPEG_PATH + "\\ffmpeg", "-y",
                "-i", sourcePath,
                "-acodec", "aac",
                "-vcodec", "libx264",
                "-preset", "slow",
                "-b:v", "2000k",
                "-bufsize", "4k",
                "-r", "24",
                targetPath};
        try {
            log.info("인코딩 시작 >>> ...");
            Process processor = Runtime.getRuntime().exec(commands);

            String line1 = null;

            BufferedReader error = new BufferedReader(
                    new InputStreamReader(processor.getErrorStream()));

            while ((line1 = error.readLine()) != null) {
                log.debug(line1);
            }
            processor.waitFor();

            int exitValue = processor.exitValue();

            if (exitValue != 0) {
                throw new RuntimeException("exit code is not 0 [" + exitValue + "]");
            }

            log.info("... <<< 인코딩 완료");

            return true;
        } catch (IOException | InterruptedException e) {
            log.error("[EncodingService] error : {}", e);
            return false;
        }
    }

}
