package com.myhome.play.model.entity;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class CategoryTest {

    @Test
    public void to_string_예외_테스트(){
        Category category = Category.builder()
                .name("카테고리")
                .directoryPath("카테고리 패스")
                .videoList(Arrays.asList(Video.builder().fileName("파일").build()))
                .build();

        assertFalse(category.toString().contains("파일"));
    }
}