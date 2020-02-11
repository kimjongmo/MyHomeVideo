package com.myhome.play.controller;

import com.myhome.play.model.network.Header;
import com.myhome.play.model.network.response.video.VideoInfoResponse;
import com.myhome.play.service.VideoApiService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(PageController.class)
public class PageControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private VideoApiService videoApiService;

    @Test
    public void getIndexPage() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<!DOCTYPE html>")))
                .andExpect(content().string(containsString("<title>우리집 넷플릭스</title>")))
                .andExpect(header().string("content-type","text/html;charset=UTF-8"));
    }

    @Test
    public void getIndexPageWithCategory() throws Exception {
        mvc.perform(get("/런닝맨"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<!DOCTYPE html>")))
                .andExpect(content().string(containsString("<title>우리집 넷플릭스</title>")))
                .andExpect(header().string("content-type","text/html;charset=UTF-8"));
    }

    @Test
    public void getRegisterPage() throws Exception {
        mvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<!DOCTYPE html>")))
                .andExpect(content().string(containsString("<title>등록 페이지</title>")))
                .andExpect(header().string("content-type","text/html;charset=UTF-8"));
    }

    @Test
    public void getPlayerPageWithInvalidTypeId() throws Exception {
        mvc.perform(get("/player/p"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getPlayerPageWithInValidData() throws Exception {
        given(videoApiService.getInfo(123L)).willReturn(Header.ERROR(""));
        mvc.perform(get("/player/123").header("Host","192.168.35.123"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<script>")));
    }

    @Test
    public void getPlayerPageWithValidData() throws Exception {
        VideoInfoResponse response = new VideoInfoResponse();
        given(videoApiService.getInfo(123L)).willReturn(Header.OK(response));
        mvc.perform(get("/player/123").header("Host","192.168.35.123"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<title>즐거운 감상하세요.</title>")))
                .andExpect(content().string(containsString("192.168.35.123:9090/video/123")))
                ;
    }




}