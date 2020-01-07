package com.myhome.play.controller;

import com.myhome.play.model.network.Header;
import com.myhome.play.model.network.request.VideoListRequest;
import com.myhome.play.model.network.response.VideoListResponse;
import com.myhome.play.model.network.response.category.CategoryListResponse;
import com.myhome.play.service.PageApiService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PageApiController.class)
public class PageApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PageApiService pageApiService;

    @Test
    public void getContentWithInvalidData() throws Exception {
        mvc.perform(get("/contents?page=0&category="))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getContentWithInvalidDataAndPage() throws Exception {
        mvc.perform(get("/contents?category=무한도전"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getContentWithValidData() throws Exception {
        List<VideoListResponse> responseData = Arrays.asList(
                VideoListResponse.builder()
                        .id(1L)
                        .view(0L)
                        .thumbnailUrl("/img/thumbnail/무한도전1화.jpg")
                        .title("무한도전1화")
                        .build());

        given(pageApiService.getContents(any())).willReturn(Header.OK(responseData));

        mvc.perform(get("/contents?category=무한도전&page=0"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("무한도전")));
    }

    @Test
    public void getCategoryWithValidData() throws Exception {
        List<CategoryListResponse> listResponses = Arrays.asList(
                CategoryListResponse.builder().id(1L).name("무한도전").build(),
                CategoryListResponse.builder().id(2L).name("런닝맨").build());

        given(pageApiService.getCategoryList()).willReturn(Header.OK(listResponses));

        mvc.perform(get("/category"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("무한도전")))
                .andExpect(content().string(containsString("런닝맨")));

    }

}