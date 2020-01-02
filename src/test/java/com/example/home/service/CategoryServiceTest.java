package com.example.home.service;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class CategoryServiceTest {

    private CategoryService categoryService;

    @Before
    public void setUp(){
        categoryService = new CategoryService();
    }

    @Test
    public void test(){
        categoryService.HOME_PATH = "D:\\MyHomeVideo";

        List<String> categoryList = categoryService.getCategoryList();

        categoryList.forEach(System.out::println);
    }

}