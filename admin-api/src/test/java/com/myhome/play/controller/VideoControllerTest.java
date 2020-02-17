package com.myhome.play.controller;


import com.myhome.play.exceptions.CategoryNotFoundException;
import com.myhome.play.model.network.Header;
import com.myhome.play.model.network.request.video.VideoModifyRequest;
import com.myhome.play.model.network.response.VideoListResponse;
import com.myhome.play.service.VideoApiService;
import com.myhome.play.utils.JsonMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(VideoController.class)
public class VideoControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private VideoApiService videoApiService;

    @Test
    public void delete_test() throws Exception {

        doNothing().when(videoApiService).delete(anyLong());

        mvc.perform(delete("/video/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("1")))
        ;

        verify(videoApiService).delete(anyLong());
    }

    @Test
    public void get_list_test() throws Exception {
        VideoListResponse response = new VideoListResponse();
        response.setTitle("제목");

        given(videoApiService.getList(anyString(), any())).willReturn(Header.OK(Arrays.asList(response)));

        mvc.perform(get("/video?category=TEST&page=0"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("제목")))
        ;

        verify(videoApiService).getList(eq("TEST"), any());
    }

    @Test
    public void category_not_found_test() throws Exception {
        given(videoApiService.getList(anyString(), any())).willThrow(new CategoryNotFoundException("카테고리"));

        mvc.perform(get("/video?category=TEST"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("카테고리")));
    }

    @Test
    //변경 요청
    public void modify_success_test() throws Exception {
        given(videoApiService.modify(any())).willReturn(Header.OK(""));

        VideoModifyRequest request = VideoModifyRequest.builder()
                .id(2L)
                .fileName("변경된 이름.mp4")
                .imgUrl("변경.jpg")
                .title("변경된 파일입니다.")
                .views(50L)
                .build();

        mvc.perform(put("/video")
                .content(JsonMapper.toJson(request))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
        ;
    }
}
