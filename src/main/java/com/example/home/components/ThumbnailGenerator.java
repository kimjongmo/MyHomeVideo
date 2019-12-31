package com.example.home.components;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
@Slf4j
@Getter
@Setter
public class ThumbnailGenerator {

    @Value("${thumbnail.path}")
    public String thumbnailPath;
    @Value("${ffmpeg.path}")
    public String ffmpegPath;

    //해당 동영상의 이미지가 있는지 확인하는 메서드
    public boolean isThumbnailExisted(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    //동영상에서 이미지를 추출하여 섬네일을 만드는 메서드
    // 원본 소스 : https://javacan.tistory.com/tag/%EC%8D%B8%EB%84%A4%EC%9D%BC%EC%B6%94%EC%B6%9C
    public void extractImage(File videoFile, int position, File creatingImageFile) {
        if(isThumbnailExisted(creatingImageFile.getAbsolutePath()))
            return;

        try {
            int seconds = position % 60;
            int minutes = (position - seconds) / 60;
            int hours = (position - minutes * 60 - seconds) / 60 / 60;

            String videoFilePath = videoFile.getAbsolutePath();
            String imageFilePath = creatingImageFile.getAbsolutePath();
            log.info("videoFilePath={}",videoFilePath);
            log.info("imageFilePath={}",imageFilePath);
            log.info("videoFileExisted={}",videoFile.exists());
            log.info("imageFileExisted={}",creatingImageFile.exists());
            String[] commands = {this.ffmpegPath+"\\ffmpeg", "-ss",
                    String.format("%02d:%02d:%02d", hours, minutes, seconds),
                    "-i", videoFilePath, "-an", "-vframes", "1", "-y",
                    imageFilePath};

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
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
