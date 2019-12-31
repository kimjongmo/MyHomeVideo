package com.example.home.components;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class ThumbnailGeneratorTest {

    @Autowired
    private ThumbnailGenerator thumbnailGenerator;

    @Before
    public void setUp(){
        thumbnailGenerator = new ThumbnailGenerator();
        thumbnailGenerator.setFfmpegPath("C:\\Users\\KIM\\Desktop\\ffmpeg-20191229-e20c6d9-win64-static\\ffmpeg-20191229-e20c6d9-win64-static\\bin");
        thumbnailGenerator.setThumbnailPath("C:\\Users\\KIM\\Desktop\\HomePlayer\\src\\main\\resources\\static\\img\\thumbnail");
    }
    @Test
    public void generatorThumbnail() throws IOException {
        String fileName = "test";
        File file = new File("D:\\MyHomeVideo\\sample\\"+fileName+".mp4");
        int position = 60;
        File creatingImageFile = new File(thumbnailGenerator.getThumbnailPath()+"\\"+fileName+".jpg");
        thumbnailGenerator.extractImage(file,position,creatingImageFile);

        assertTrue(creatingImageFile.exists());
    }

    @Test
    public void isThumbnailExisted(){
        String fileName = "test";
        assertTrue(thumbnailGenerator.isThumbnailExisted(thumbnailGenerator.getThumbnailPath()+"\\"+fileName+".jpg"));
    }

    @Test
    public void isThumbnailNotExisted(){
        String fileName = "test1234";
        assertFalse(thumbnailGenerator.isThumbnailExisted(thumbnailGenerator.getThumbnailPath()+"\\"+fileName+".jpg"));
    }
}