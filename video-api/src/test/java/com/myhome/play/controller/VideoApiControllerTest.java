package com.myhome.play.controller;

import com.myhome.play.exceptions.CategoryNotFoundException;
import com.myhome.play.exceptions.FileDuplicateException;
import com.myhome.play.model.network.Header;
import com.myhome.play.model.network.request.video.VideoInsertRequest;
import com.myhome.play.model.network.response.VideoListResponse;
import com.myhome.play.service.ThumbnailService;
import com.myhome.play.service.VideoApiService;
import com.myhome.play.utils.JsonMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(VideoApiController.class)
public class VideoApiControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private VideoApiService videoApiService;
    @MockBean
    private ThumbnailService thumbnailService;

    @Test
    public void get_list_with_invalid_data() throws Exception {
        List<VideoListResponse> list = new ArrayList<>();

        given(videoApiService.getList(anyString(), any())).willReturn(Header.OK(list));

        mvc.perform(get("/video?category=무한도전&page=1"))
                .andExpect(status().isOk())
        ;
    }

    @Test
    public void get_list_with_not_exist_category_request() throws Exception {
        given(videoApiService.getList(anyString(), any())).willThrow(new CategoryNotFoundException("TEST"));

        mvc.perform(get("/video?category=TEST&page=1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("TEST")));
    }

    @Test
    public void get_list_with_request_no_category() throws Exception {
        mvc.perform(get("/video?page=1")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("category")));
    }

    @Test
    public void get_list_with_request_no_page() throws Exception {
        List<VideoListResponse> list = new ArrayList<>();

        given(videoApiService.getList(anyString(), any())).willReturn(Header.OK(list));

        mvc.perform(get("/video?category=테스트")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
        ;
    }


    // TODO: 2020-01-09 404 Error나는 것 나중에 잡기
//    @Test
//    public void insert_with_duplicate_file() throws Exception {
//        String fileName = "중복.mp4";
//        given(videoApiService.insert(any())).willThrow(new FileDuplicateException(fileName));
//
//        VideoInsertRequest request = new VideoInsertRequest();
//        request.setFileName(fileName);
//        request.setTitle("중복입니다.");
//        request.setCategoryName("카테고리");
//
//        mvc.perform(post("/video")
//                .content(""))
////                .content(JsonMapper.toJson(request)))
//                .andExpect(status().isOk())
//                .andExpect(content().string(containsString(fileName)))
//                ;
//    }

}