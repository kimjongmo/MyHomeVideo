package com.myhome.play.utils;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class FileUtilsTest {
    private FileUtils fileUtils;

    @Before
    public void setUp(){
        fileUtils = new FileUtils();
        ReflectionTestUtils.setField(fileUtils, "HOME_PATH", "D:/MyHomeVideo");
    }

    @Test
    public void exist_file_delete_test(){
        FileUtils mockFileUtils = spy(fileUtils);
        File mockFile = mock(File.class);

        //given
        doReturn(mockFile).when(mockFileUtils).getFile(anyString(),anyString());
        doReturn(true).when(mockFile).exists();
        doReturn(true).when(mockFile).delete();

        //when
        boolean result = mockFileUtils.delete("TEST","test.mp4");

        //then
        assertTrue(result);
    }

    @Test
    public void not_existed_delete_test(){
        FileUtils mockFileUtils = spy(fileUtils);
        File mockFile = mock(File.class);

        //given
        doReturn(mockFile).when(mockFileUtils).getFile(anyString(),anyString());
        doReturn(false).when(mockFile).exists();

        //when
        boolean result = mockFileUtils.delete("TEST","test.mp4");

        //then
        assertTrue(result);
    }

    @Test
    public void get_ext_test(){
        String result = fileUtils.getExt("test.mp4");
        assertEquals(result,"mp4");

        result = fileUtils.getExt("test1.test2.test3.mp4");
        assertEquals(result,"mp4");

        result = fileUtils.getExt("test1");
        assertEquals(result,"");
    }

    @Test
    public void get_pure_name_test(){
        String result = fileUtils.getPureName("test.mp4");
        assertEquals(result,"test");

        result = fileUtils.getPureName("mp4.test.mp4");
        assertEquals(result,"mp4.test");

        result = fileUtils.getPureName("test");
        assertEquals(result,"test");
    }
}