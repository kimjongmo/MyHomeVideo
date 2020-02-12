package com.myhome.play.service;

import com.myhome.play.exceptions.CategoryNotFoundException;
import com.myhome.play.exceptions.DataSizeNotMatchException;
import com.myhome.play.exceptions.FileDuplicateException;
import com.myhome.play.model.entity.Category;
import com.myhome.play.model.network.Header;
import com.myhome.play.repo.CategoryRepository;
import com.myhome.play.serivce.FileUploadService;
import com.myhome.play.serivce.MessageProducerService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


public class FileUploadServiceTest {

    private FileUploadService fileUploadService;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private RestTemplateService restTemplateService;
    @Mock
    private MessageProducerService messageProducerService;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        fileUploadService = new FileUploadService(categoryRepository, restTemplateService,messageProducerService);
        ReflectionTestUtils.setField(fileUploadService, "ROOT_PATH", "D:/MyHomeVideo");
        ReflectionTestUtils.setField(fileUploadService, "videoServerIp", "http://192.168.35.239:9090");
    }

    @Test(expected = DataSizeNotMatchException.class)
    public void validate_test_with_data_size_not_match() {

        given(categoryRepository.findById(1L)).willReturn(Optional.of(Category.builder().build()));

        List<MultipartFile> fileList = Arrays.asList(
                new MockMultipartFile("file1",
                        "filename1.mp4",
                        "multipart/form-data",
                        "test".getBytes()),
                new MockMultipartFile("file2",
                        "filename2.mp4",
                        "multipart/form-data",
                        "test".getBytes())
        );

        Long categoryId = 1L;

        List<String> titles = Arrays.asList("파일1");

        fileUploadService.validate(fileList, categoryId, titles);

    }

    @Test(expected = CategoryNotFoundException.class)
    public void validate_test_with_not_existed_category() {

        given(categoryRepository.findById(1L)).willReturn(Optional.empty());

        List<MultipartFile> fileList = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        Long categoryId = 1L;
        fileUploadService.validate(fileList, categoryId, titles);
    }


    @Test
    public void upload_success_test() throws IOException {

        FileUploadService mockFileUploadService = Mockito.spy(fileUploadService);

        //INPUT
        Long categoryId = 1L;

        List<MultipartFile> multipartFiles = new ArrayList<>();
        multipartFiles.add(new MockMultipartFile("file",
                "filename.mp4",
                "video/mp4",
                "This is content about video".getBytes()));

        multipartFiles.add(new MockMultipartFile("file",
                "filename.avi",
                "video/avi",
                "This is content about video".getBytes()));

        List<String> titles = new ArrayList<>();
        titles.add("테스트입니다.");
        titles.add("테스트입니다.");

        //GIVEN
        given(categoryRepository.findById(categoryId))
                .willReturn(Optional.of(Category.builder().name("테스트").build()));

        doReturn(Header.MESSAGE("SUCCESS")).when(mockFileUploadService)
                .insert(any());

        doNothing().when(mockFileUploadService)
                .fileUpload(any(), anyString());

        //WHEN
        Header header = mockFileUploadService.upload(multipartFiles,categoryId,titles);

        //THEN
        assertTrue(header.getStatus().equals("OK"));
        assertTrue(header.getDescription().contains("filename.mp4가 등록되었습니다."));

    }

    @Test
    public void upload_fail_with_duplicate() throws IOException {
        FileUploadService mockFileUploadService = Mockito.spy(fileUploadService);

        //INPUT
        Long categoryId = 1L;

        List<MultipartFile> multipartFiles = new ArrayList<>();
        multipartFiles.add(new MockMultipartFile("file",
                "filename.mp4",
                "multipart/form-data",
                "test".getBytes()));

        List<String> titles = new ArrayList<>();
        titles.add("테스트입니다.");


        //GIVEN
        given(categoryRepository.findById(categoryId))
                .willReturn(Optional.of(Category.builder().name("테스트").build()));


        doThrow(new FileDuplicateException())
                .doNothing()
                .when(mockFileUploadService)
                .fileUpload(any(), anyString());

        //WHEN
        Header result = mockFileUploadService.upload(multipartFiles, 1L, titles);
        System.out.println(result);
        assertEquals(result.getStatus(), "OK");
        assertTrue(result.getDescription().contains("중복"));
    }

    @Test
    public void rabbit_mq_message_send_test() throws IOException {
        FileUploadService mockFileUploadService = Mockito.spy(fileUploadService);

        //INPUT
        Long categoryId = 1L;

        List<MultipartFile> multipartFiles = new ArrayList<>();

        multipartFiles.add(new MockMultipartFile("file",
                "filename.avi",
                "video/avi",
                "This is content about video".getBytes()));

        List<String> titles = new ArrayList<>();
        titles.add("테스트입니다.");

        //GIVEN
        Category mockCategory = Category.builder().name("TEST").build();
        given(categoryRepository.findById(categoryId)).willReturn(Optional.of(mockCategory));
        doNothing().when(mockFileUploadService).validate(anyList(),anyLong(),anyList());
        doNothing().when(mockFileUploadService).fileUpload(any(),anyString());
        doNothing().when(messageProducerService).sendTo(anyString(),anyString(), );

        mockFileUploadService.upload(multipartFiles,categoryId,titles);
        verify(messageProducerService).sendTo(eq("TEST"),eq("filename.avi"), );
    }
}