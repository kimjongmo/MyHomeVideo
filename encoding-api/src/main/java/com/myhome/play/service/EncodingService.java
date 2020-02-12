package com.myhome.play.service;

import com.myhome.play.enums.EncodingResult;
import com.myhome.play.model.network.Header;
import com.myhome.play.model.network.request.encode.EncodeRequestDTO;
import com.myhome.play.model.network.request.video.VideoInsertRequest;
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

@Service
@Slf4j
public class EncodingService {

    @Value("${ffmpeg.path}")
    public String FFMPEG_PATH;

    @Value("${home.path}")
    public String HOME_PATH;

    @Value("${video.server.ip}")
    public String videoServerIp;

    private RestTemplateService restTemplateService;

    public EncodingService(RestTemplateService restTemplateService) {
        this.restTemplateService = restTemplateService;
    }

    /**
     * 비디오 파일을 MPEG-4 타입으로 인코딩한다.
     *
     * @date 2020.02.09
     * @author kimjongmo
     */
    public EncodingResult encodingToMPEG4(EncodeRequestDTO requestDTO) {

        //Validate Input
        if (!validate(requestDTO))
            return EncodingResult.INPUT_ERROR;

        File file = getFile(requestDTO.getCategory(), requestDTO.getName());

        //file is not existed
        if (file == null)
            return EncodingResult.INPUT_ERROR;

        String sourcePath = file.getAbsolutePath();//인코딩할 파일의 절대경로
        String targetPath = removeExt(file) + ".mp4"; //인코딩 된 파일의 목적 경로

        //비디오 인코딩
        boolean isSuccess = encodingVideo(sourcePath, targetPath);
        if (!isSuccess)
            return EncodingResult.ERROR;

        // VideoInsertRequest 객체를 생성 후 video 서버에 전달.
        Header header
                = insert(makeRequestData(requestDTO.getCategory(), requestDTO.getTitle(), file.getName()));

        //avi 파일 삭제
        fileDelete(requestDTO.getName(), requestDTO.getCategory());

        if (header.getDescription().equals("SUCCESS")) {
            return EncodingResult.OK;
        }

        //메타데이터 등록 실패 시
        fileDelete(file.getName(), requestDTO.getCategory());
        return EncodingResult.SAVE_META_DATA_FAIL;
    }

    //파일을 가져온다.
    public File getFile(String category, String source) {
        File file = new File(HOME_PATH + "\\" + category + "\\" + source);
        if (!file.exists())
            return null;
        return file;
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
     * 파일의 확장자를 제외한 이름을 리턴한다.
     *
     * @param file 파일
     * @return 전체 경로를 포함한 파일의 순수 이름
     * @date 2020.02.09
     * @author kimjongmo
     */
    public String removeExt(File file) {
        return file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf("."));
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

    /**
     * 카테고리에 존재하는 파일을 삭제한다.
     *
     * @param name     제거할 파일의 이름
     * @param category 제거할 파일의 카테고리
     * @return <code>true</code> 삭제; <code>false</code> 삭제 실패
     */
    public boolean fileDelete(String name, String category) {
        File file = new File(HOME_PATH + "/" + category + "/" + name);
        if (file.exists()) {
            return file.delete();
        }
        return true;
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
