package com.example.home.components;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

public class VideoUtilsTest {

    private VideoUtils videoUtils;

    @Before
    public void setUp() {
        videoUtils = new VideoUtils();
        videoUtils.HOME_PATH = "D:\\MyHomeVideo";
    }

    @Test
    public void pageTest() {
        String category = "런닝맨";
        try {
            for(int i=0;i<10;i++){
                List<File> fileList = videoUtils.getFileList(category, i);
                fileList.forEach(System.out::println);
                System.out.println(fileList.size()+"개");
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("없는 페이지입니다.");
        }
    }

}