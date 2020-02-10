package com.myhome.play.service;

import com.myhome.play.enums.EncodingResult;
import com.myhome.play.model.network.request.encode.EncodeRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
@Slf4j
public class EncodingService {

    @Value("${ffmpeg.path}")
    public String FFMPEG_PATH;

    @Value("${home.path}")
    public String HOME_PATH;


    /**
     * 비디오 파일을 MPEG-4 타입으로 인코딩한다.
     * @date 2020.02.09
     * @author kimjongmo
     */
    public EncodingResult encodingToMPEG4(EncodeRequestDTO requestDTO) {

        String category = requestDTO.getCategory();
        String source = requestDTO.getName();

        //카테고리 혹은 파일이름이 비어있으면 실행시키지 않는다.
        if (category == null || category.isEmpty()) return EncodingResult.INPUT_ERROR;
        if (source == null || source.isEmpty()) return EncodingResult.INPUT_ERROR;

        //파일이 존재하지 않으면 더 이상 진행하지 않는다.
        File file = new File(HOME_PATH + "\\" + category + "\\" + source);
        if (!file.exists()) return EncodingResult.INPUT_ERROR;

        String fullSource = file.getAbsolutePath();
        String fullTarget = removeExt(file) + ".mp4";

        String[] commands = {FFMPEG_PATH + "\\ffmpeg", "-y",
                "-i", fullSource,
                "-acodec","aac",
                "-vcodec","libx264",
                "-preset","slow",
                "-b:v","2000k",
                "-bufsize","4k",
                "-r","24",
                fullTarget};
        try {
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

            return EncodingResult.OK;
        } catch (IOException | InterruptedException e) {
            log.error("[EncodingService] error : {}", e);
            return EncodingResult.ERROR;
        }

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
}
