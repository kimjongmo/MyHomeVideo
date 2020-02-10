package com.myhome.play.service;


import com.myhome.play.enums.EncodingResult;
import com.myhome.play.model.network.request.encode.EncodeRequestDTO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class EncodingServiceTest {

    private EncodingService encodingService;

    @Before
    public void setUp() {
        encodingService = new EncodingService();
        ReflectionTestUtils.setField(encodingService, "HOME_PATH", "D:/MyHomeVideo");
        ReflectionTestUtils.setField(encodingService, "FFMPEG_PATH", "C:\\Users\\KIM\\Desktop\\ffmpeg-20191229-e20c6d9-win64-static\\ffmpeg-20191229-e20c6d9-win64-static\\bin");
    }

    @Test
    public void get_file_pure_name_with_valid_input() {
        File mockFile = Mockito.mock(File.class);
        when(mockFile.getAbsolutePath()).thenReturn("D:\\test\\file.avi");
        when(mockFile.exists()).thenReturn(true);

        String result = encodingService.removeExt(mockFile);
        assertTrue(result.equals("D:\\test\\file"));
    }

    @Test
    public void invalid_input_test() {
        EncodingResult result;
        result = encodingService.encodingToMPEG4(
                EncodeRequestDTO.builder().category("").name("test").build());
        assertTrue(result.equals(EncodingResult.INPUT_ERROR));

        result = encodingService.encodingToMPEG4(
                EncodeRequestDTO.builder().category("test").name("").build());
        assertTrue(result.equals(EncodingResult.INPUT_ERROR));

        result = encodingService.encodingToMPEG4(
                EncodeRequestDTO.builder().name("test").build());
        assertTrue(result.equals(EncodingResult.INPUT_ERROR));

        result = encodingService.encodingToMPEG4(
                EncodeRequestDTO.builder().category("").build());
        assertTrue(result.equals(EncodingResult.INPUT_ERROR));
    }

}
