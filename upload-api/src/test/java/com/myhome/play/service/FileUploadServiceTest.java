package com.myhome.play.service;

import com.myhome.play.exceptions.CategoryNotFoundException;
import com.myhome.play.exceptions.DataSizeNotMatchException;
import com.myhome.play.model.entity.Category;
import com.myhome.play.repo.CategoryRepository;
import com.myhome.play.repo.VideoRepository;
import com.myhome.play.serivce.FileUploadService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.MapKeyColumn;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;


public class FileUploadServiceTest {

    private FileUploadService fileUploadService;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private VideoRepository videoRepository;
    @Mock
    private RestTemplateService restTemplateService;


    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        fileUploadService = new FileUploadService(categoryRepository,videoRepository,restTemplateService);
        ReflectionTestUtils.setField(fileUploadService, "ROOT_PATH", "D:/MyHomeVideo");
    }

    @Test(expected = DataSizeNotMatchException.class)
    public void validate_test_with_data_size_not_match(){

        BDDMockito.given(categoryRepository.findById(1L)).willReturn(Optional.of(Category.builder().build()));

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

        fileUploadService.validate(fileList,categoryId,titles);

    }

    @Test(expected = CategoryNotFoundException.class)
    public void validate_test_with_not_existed_category(){

        BDDMockito.given(categoryRepository.findById(1L)).willReturn(Optional.empty());

        List<MultipartFile> fileList = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        Long categoryId = 1L;
        fileUploadService.validate(fileList,categoryId,titles);
    }

    // TODO: 2020-01-25 insert 실패 시 처리
}