package com.myhome.play.model.entity;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class VideoTest {

    @Test
    public void to_string_예외_테스트() {
        Video video = Video.builder()
                .views(0L)
                .imgUrl("/img/thumbnail/1")
                .fileName("filename")
                .title("title")
                .category(Category.builder().name("category").build())
                .build();

        assertFalse(video.toString().contains("category"));
    }
}