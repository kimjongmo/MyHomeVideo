package com.myhome.play.components;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ThumbnailGeneratorTest {

    @Autowired
    private ThumbnailGenerator thumbnailGenerator;

    @Before
    public void setUp() {
        thumbnailGenerator = new ThumbnailGenerator();

        thumbnailGenerator.FFMPEG_PATH = "C:\\Users\\KIM\\Desktop\\ffmpeg-20191229-e20c6d9-win64-static\\ffmpeg-20191229-e20c6d9-win64-static\\bin";
        thumbnailGenerator.THUMBNAIL_PATH = "C:\\Users\\KIM\\Desktop\\HomePlayer\\front\\src\\main\\resources\\static\\img\\thumbnail";
    }

    @After
    public void rollback(){

    }
    @Test
    public void generatorThumbnail() throws IOException {
        String fileName = "test";
        File file = new File("D:\\MyHomeVideo\\sample\\" + fileName + ".mp4");
        int position = 60;
        File creatingImageFile = new File(thumbnailGenerator.THUMBNAIL_PATH + "\\" + fileName + ".jpg");
        thumbnailGenerator.extractImage(file, position, creatingImageFile);

        assertTrue(creatingImageFile.exists());
    }

    @Test
    public void isThumbnailExisted() {
        String fileName = "test";
        assertTrue(thumbnailGenerator.isThumbnailExisted(fileName));
    }

    @Test
    public void removeThumbnail(){
        String fileName = "test";
        thumbnailGenerator.removeThumbnail(fileName);
        assertFalse(thumbnailGenerator.isThumbnailExisted(fileName));
    }

}