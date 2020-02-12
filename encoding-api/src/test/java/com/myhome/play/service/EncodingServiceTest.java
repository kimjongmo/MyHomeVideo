package com.myhome.play.service;


import com.myhome.play.enums.EncodingResult;
import com.myhome.play.model.network.Header;
import com.myhome.play.model.network.request.encode.EncodeRequestDTO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class EncodingServiceTest {

    private EncodingService encodingService;

    @Mock
    private RestTemplateService restTemplateService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        encodingService = new EncodingService(restTemplateService);
        ReflectionTestUtils.setField(encodingService, "HOME_PATH", "D:/MyHomeVideo");
        ReflectionTestUtils.setField(encodingService, "FFMPEG_PATH", "C:\\Users\\KIM\\Desktop\\ffmpeg-20191229-e20c6d9-win64-static\\ffmpeg-20191229-e20c6d9-win64-static\\bin");
        ReflectionTestUtils.setField(encodingService, "videoServerIp", "http://localhost:9090");
    }

    @Test
    //removeExt 메서드 테스트
    public void get_file_pure_name_with_valid_input() {
        File mockFile = Mockito.mock(File.class);
        when(mockFile.getAbsolutePath()).thenReturn("D:\\test\\file.avi");
        when(mockFile.exists()).thenReturn(true);

        String result = encodingService.removeExt(mockFile);
        assertTrue(result.equals("D:\\test\\file"));
    }

    @Test
    //존재하지 않는 파일이라면
    public void get_file_with_not_exist() {
        EncodingService mockService = spy(encodingService);

        //INPUT
        EncodeRequestDTO dto = EncodeRequestDTO.builder()
                .category("TEST")
                .name("test.avi")
                .title("제목")
                .build();

        //GIVEN
        doReturn(null).when(mockService).getFile(anyString(),anyString());

        //WHEN
        EncodingResult result = mockService.encodingToMPEG4(dto);

        //THEN
        assertEquals(result,EncodingResult.INPUT_ERROR);
    }


    @Test
    //validate 테스트
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

    @Test
    //인코딩 성공 후 메타데이터를 저장하는지
    public void action_metadata_save_after_encoding() {
        EncodingService mockEncodingService = spy(encodingService);

        //INPUT
        EncodeRequestDTO dto = EncodeRequestDTO.builder()
                .title("테스트입니다")
                .name("테스트.avi")
                .category("TEST")
                .build();
        //GIVEN
        File mockFile = mock(File.class);

        doReturn(mockFile).when(mockEncodingService).getFile(anyString(), anyString());
        given(mockFile.getAbsolutePath()).willReturn("D:/MyHomeVideo/TEST/테스트.avi");
        doReturn(true).when(mockEncodingService).encodingVideo(anyString(), anyString());
        given(restTemplateService.exchange(any(), eq(HttpMethod.POST), any(), any())).willReturn(Header.MESSAGE("SUCCESS"));
        doReturn(true).when(mockEncodingService).fileDelete(anyString(), anyString());

        //WHEN
        EncodingResult result = mockEncodingService.encodingToMPEG4(dto);

        //THEN
        assertEquals(result, EncodingResult.OK);
        verify(mockEncodingService).insert(any());
        verify(mockEncodingService).fileDelete(eq("테스트.avi"), eq("TEST"));
    }

    @Test
    //메타 데이터 정보를 등록하는 것에 실패 후에 mp4, avi 파일 둘다 삭제하는지
    public void action_file_delete_after_save_fail() {
        EncodingService mockEncodingService = spy(encodingService);

        //INPUT
        EncodeRequestDTO dto = EncodeRequestDTO.builder()
                .title("테스트입니다")
                .name("테스트.avi")
                .category("TEST")
                .build();
        //GIVEN
        File mockFile = mock(File.class);
        doReturn("테스트.mp4").when(mockFile).getName();
        doReturn(mockFile).when(mockEncodingService).getFile(anyString(), anyString());
        given(mockFile.getAbsolutePath()).willReturn("D:/MyHomeVideo/TEST/테스트.avi");
        doReturn(true).when(mockEncodingService).encodingVideo(anyString(), anyString());
        given(restTemplateService.exchange(any(), eq(HttpMethod.POST), any(), any())).willReturn(Header.ERROR("FAIL"));
        doReturn(true).when(mockEncodingService).fileDelete(anyString(), anyString());
        //WHEN
        EncodingResult result = mockEncodingService.encodingToMPEG4(dto);

        //THEN
        assertEquals(result, EncodingResult.SAVE_META_DATA_FAIL);
        verify(mockEncodingService).insert(any());
        verify(mockEncodingService).fileDelete(eq("테스트.avi"), eq("TEST"));
        verify(mockEncodingService).fileDelete(eq("테스트.mp4"), eq("TEST"));
    }

}
