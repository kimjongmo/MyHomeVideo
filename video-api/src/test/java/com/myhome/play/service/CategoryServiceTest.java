package com.myhome.play.service;

import com.myhome.play.model.entity.Category;
import com.myhome.play.model.network.response.category.CategoryListResponse;
import com.myhome.play.repo.CategoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;

public class CategoryServiceTest {

    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        categoryService = new CategoryService(categoryRepository);
        ReflectionTestUtils.setField(categoryService, "HOME_PATH", "D:/MyHomeVideo");
    }

    @Test
    public void test(){
        List<Category> list = new ArrayList<>();
        given(categoryRepository.findAll()).willReturn(list);
        categoryService.HOME_PATH = "D:\\MyHomeVideo";
        List<CategoryListResponse> categoryList = categoryService.getCategoryList();
    }
}
