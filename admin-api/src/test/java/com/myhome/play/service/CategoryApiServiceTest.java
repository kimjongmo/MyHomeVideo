package com.myhome.play.service;

import com.myhome.play.model.entity.Category;
import com.myhome.play.model.network.Header;
import com.myhome.play.model.network.request.category.CategoryModifyRequest;
import com.myhome.play.model.network.response.category.CategoryListResponse;
import com.myhome.play.model.network.response.category.CategoryModifyResponse;
import com.myhome.play.repo.CategoryRepository;
import com.myhome.play.utils.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class CategoryApiServiceTest {

    private CategoryApiService categoryApiService;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private FileUtils fileUtils;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        categoryApiService = new CategoryApiService(categoryRepository, fileUtils);
    }

    @Test
    //카테고리 리스트 테스트
    public void get_category_list() {
        List<Category> categoryList = Arrays.asList(
                Category.builder().name("TEST").build(),
                Category.builder().name("SAMPLE").build()
        );

        given(categoryRepository.findAll()).willReturn(categoryList);

        List<CategoryListResponse> response = categoryApiService.getCategoryList();

        assertEquals(response.size(), 2);
        assertEquals(response.get(0).getName(), "TEST");
        assertEquals(response.get(1).getName(), "SAMPLE");
    }


    @Test
    //카테고리 수정 성공
    public void modify_category_success_test() {
        //INPUT
        CategoryModifyRequest request =
                CategoryModifyRequest.builder().id(1L).name("변경 후").build();

        //GIVEN
        Category category = Category.builder().name("변경 전").build();
        category.setId(1L);

        File mockDirectory = mock(File.class);

        given(mockDirectory.getName()).willReturn(category.getName());
        given(mockDirectory.exists()).willReturn(true);
        given(mockDirectory.isDirectory()).willReturn(true);
        given(mockDirectory.renameTo(any())).willReturn(true);

        given(categoryRepository.findById(1L)).willReturn(Optional.of(category));
        given(categoryRepository.save(any())).will(invocation -> {
            return invocation.getArgument(0);
        });
        given(fileUtils.getCategory("변경 전")).willReturn(mockDirectory);

        //WHEN
        Header<CategoryModifyResponse> header = categoryApiService.modify(request);

        //THEN
        assertEquals(header.getStatus(), "OK");
        assertEquals(header.getData().getId(), Long.valueOf(1L));
        assertEquals(header.getData().getName(), "변경 후");
    }

    @Test
    //등록되어 있지 않는 데이터
    public void modify_category_with_not_exist_data() {
        //INPUT
        CategoryModifyRequest request =
                CategoryModifyRequest.builder().id(1L).name("변경 후").build();

        //GIVEN
        given(categoryRepository.findById(anyLong())).willReturn(Optional.empty());

        //WHEN
        Header header = categoryApiService.modify(request);

        //THEN
        assertEquals(header.getStatus(), "ERROR");
    }

    @Test
    //등록되어 있는 데이터지만 폴더가 존재하지 않을 때
    public void modify_category_with_not_exist_folder() {
        //INPUT
        CategoryModifyRequest request =
                CategoryModifyRequest.builder().id(1L).name("변경 후").build();

        //GIVEN
        Category category = Category.builder().name("변경 전").build();
        category.setId(1L);

        File mockDirectory = mock(File.class);

        given(mockDirectory.getName()).willReturn(category.getName());
        given(mockDirectory.exists()).willReturn(false);
        given(mockDirectory.mkdir()).willReturn(true);
        given(mockDirectory.renameTo(any())).willReturn(true);

        given(categoryRepository.findById(1L)).willReturn(Optional.of(category));
        given(categoryRepository.save(any())).will(invocation -> {
            return invocation.getArgument(0);
        });
        given(fileUtils.getCategory("변경 전")).willReturn(mockDirectory);

        //WHEN
        Header<CategoryModifyResponse> header = categoryApiService.modify(request);

        //THEN
        assertEquals(header.getStatus(), "OK");
        assertEquals(header.getData().getId(), Long.valueOf(1L));
        assertEquals(header.getData().getName(), "변경 후");
    }

    @Test
    //바꾸려는 이름이 이미 존재할 때.
    public void modify_category_with_rename_folder_already_exist() {
        //INPUT
        CategoryModifyRequest request =
                CategoryModifyRequest.builder().id(1L).name("변경 후").build();

        //GIVEN
        Category category = Category.builder().name("변경 전").build();
        category.setId(1L);

        File mockDirectory = mock(File.class);

        given(mockDirectory.getName()).willReturn(category.getName());
        given(mockDirectory.exists()).willReturn(true);
        given(mockDirectory.isDirectory()).willReturn(true);
        given(mockDirectory.renameTo(any())).willReturn(false);

        given(categoryRepository.findById(1L)).willReturn(Optional.of(category));
        given(categoryRepository.save(any())).will(invocation -> {
            return invocation.getArgument(0);
        });
        given(fileUtils.getCategory("변경 전")).willReturn(mockDirectory);

        //WHEN
        Header<CategoryModifyResponse> header = categoryApiService.modify(request);

        //THEN
        assertEquals(header.getStatus(), "ERROR");
        assertTrue(header.getDescription().contains("이미 존재하는"));
    }

    @Test
    // 카테고리 삭제 성공
    public void delete_category_test() {
        Long id = 1L;

        Category mockCategory = Category.builder().name("TEST").build();
        mockCategory.setId(id);
        File directory = mock(File.class);//폴더

        //디렉토리 안에 파일들 세팅
        File[] files = new File[5];
        for (int i = 0; i < 5; i++) {
            files[i] = mock(File.class);
            given(files[i].delete()).willReturn(true);
        }


        //GIVEN
        given(categoryRepository.findById(id)).willReturn(Optional.of(mockCategory));//검색
        given(fileUtils.getCategory(mockCategory.getName())).willReturn(directory); //폴더 리턴
        given(directory.listFiles()).willReturn(files); //내부 파일들
        given(directory.delete()).willReturn(true);
        given(directory.exists()).willReturn(true);
        given(directory.isDirectory()).willReturn(true);
        doNothing().when(categoryRepository).deleteById(id);//삭제

        Header header = categoryApiService.delete(id);

        assertEquals(header.getStatus(), "OK");
        assertEquals(header.getDescription(), "삭제되었습니다");
        verify(categoryRepository).findById(1L);
        verify(categoryRepository).deleteById(1L);
    }

    @Test
    // 없는 정보인 경우
    public void delete_category_with_not_existed_data() {
        //INPUT
        Long id = 1L;

        //GIVEN
        given(categoryRepository.findById(id)).willReturn(Optional.empty());

        //WHEN
        Header result = categoryApiService.delete(id);

        //THEN
        assertEquals(result.getStatus(), "ERROR");
        assertTrue(result.getDescription().contains("존재"));
    }

    @Test
    // 파일 삭제 실패
    public void delete_category_with_fail_remove_file() {
        Long id = 1L;

        Category mockCategory = Category.builder().name("TEST").build();
        mockCategory.setId(id);
        File directory = mock(File.class);//폴더

        //디렉토리 안에 파일들 세팅
        File[] files = new File[5];
        for (int i = 0; i < 5; i++) {
            files[i] = mock(File.class);
            if(i==3) given(files[i].delete()).willReturn(false);//3번 파일 삭제 실패
            else given(files[i].delete()).willReturn(true);
        }

        //GIVEN
        given(categoryRepository.findById(id)).willReturn(Optional.of(mockCategory));//검색
        given(fileUtils.getCategory(mockCategory.getName())).willReturn(directory); //폴더 리턴
        given(directory.listFiles()).willReturn(files); //내부 파일들
        given(directory.exists()).willReturn(true);
        given(directory.isDirectory()).willReturn(true);

        Header header = categoryApiService.delete(id);

        assertEquals(header.getStatus(), "ERROR");
        assertTrue(header.getDescription().contains("파일"));
        verify(directory).listFiles();
        verify(files[4],never()).delete();
    }

    @Test
    // 카테고리를 삭제할 때 폴더삭제 실패시
    public void delete_category_with_fail_remove_folder(){
        Long id = 1L;

        Category mockCategory = Category.builder().name("TEST").build();
        mockCategory.setId(id);
        File directory = mock(File.class);//폴더

        //디렉토리 안에 파일들 세팅
        File[] files = new File[5];
        for (int i = 0; i < 5; i++) {
            files[i] = mock(File.class);
            given(files[i].delete()).willReturn(true);
        }

        //GIVEN
        given(categoryRepository.findById(id)).willReturn(Optional.of(mockCategory));//검색
        given(fileUtils.getCategory(mockCategory.getName())).willReturn(directory); //폴더 리턴
        given(directory.listFiles()).willReturn(files); //내부 파일들
        given(directory.exists()).willReturn(true);
        given(directory.isDirectory()).willReturn(true);
        given(directory.delete()).willReturn(false);

        Header header = categoryApiService.delete(id);

        assertEquals(header.getStatus(), "ERROR");
        assertTrue(header.getDescription().contains("폴더"));
        verify(directory).listFiles();
    }
}