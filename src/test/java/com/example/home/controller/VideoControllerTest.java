package com.example.home.controller;

import com.example.home.exceptions.CategoryNotFoundException;
import com.example.home.model.network.Header;
import com.example.home.model.network.request.VideoListRequest;
import com.example.home.model.network.response.VideoListResponse;
import com.example.home.service.VideoService;
import org.hamcrest.core.StringContains;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(VideoController.class)
public class VideoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private VideoService videoService;

    @Test
    public void getListWithNoCategory() throws Exception {
        mvc.perform(get("/video"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getListWithCategory() throws Exception {
        String category = "런닝맨";
        String fileName = "런닝맨 1화";
        String thumbnailName = fileName+".jpg";
        List<VideoListResponse> list = new ArrayList<>();

        list.add(VideoListResponse.builder()
                .name(fileName)
                .thumbnailUrl("D:\\MyHomeService\\"+category+"\\"+thumbnailName)
                .build());
        given(videoService.getList(any())).willReturn(Header.OK(list));

        mvc.perform(get("/video?category="+category))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(fileName)))
                .andExpect(content().string(containsString(thumbnailName)));

        verify(videoService).getList(any());
    }

    @Test
    public void getListWithNotExistedCategory() throws Exception {
        String category = "무한도전";
        given(videoService.getList(any())).willThrow(new CategoryNotFoundException(category));

        mvc.perform(get("/video?category="+category))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(category)));

        verify(videoService).getList(any());
    }

}