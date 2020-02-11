package com.myhome.play.controller;

import com.myhome.play.exceptions.CategoryNotFoundException;
import com.myhome.play.exceptions.FileDuplicateException;
import com.myhome.play.model.entity.Video;
import com.myhome.play.model.network.Header;
import com.myhome.play.model.network.request.video.VideoInsertRequest;
import com.myhome.play.model.network.response.VideoListResponse;
import com.myhome.play.model.network.response.video.VideoInfoResponse;
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

    @Test
    public void get_info_with_valid_request() throws Exception {
        Long id = 1L;
        given(videoApiService.getInfo(id)).willReturn(Header.OK(VideoInfoResponse.builder().id(1L).build()));

        mvc.perform((get("/video/info")).param("id", id.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(id.toString())))
        ;
    }

    @Test
    public void get_info_with_invalid_request() throws Exception {
        Long id = 1L;

        given(videoApiService.getInfo(id)).willReturn(Header.ERROR("존재하지 않는 데이터"));

        mvc.perform((get("/video/info")).param("id", id.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("ERROR")))
                .andExpect(content().string(containsString("존재하지")))
        ;
    }

    @Test
    public void get_info_with_valid_multi_request() {

    }

    @Test
    public void input_video_extension_avi() throws Exception {

        VideoInsertRequest request = new VideoInsertRequest();
        request.setTitle("제목");
        request.setCategoryName("테스트");
        request.setFileName("이름.avi");

        given(videoApiService.insert(any())).willReturn(Video.builder().build());

        mvc.perform(post("/video")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonMapper.toJson(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("SUCCESS")));
    }

    @Test
    public void input_video_extension_mp4() throws Exception {

        VideoInsertRequest request = new VideoInsertRequest();
        request.setTitle("제목");
        request.setCategoryName("테스트");
        request.setFileName("이름.mp4");

        given(videoApiService.insert(any())).willReturn(Video.builder().build());

        mvc.perform(post("/video")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonMapper.toJson(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("SUCCESS")));
    }
    @Test
    public void insert_with_duplicate_file() throws Exception {
        String fileName = "중복.mp4";
        given(videoApiService.insert(any())).willThrow(new FileDuplicateException(fileName));

        VideoInsertRequest request = new VideoInsertRequest();
        request.setFileName(fileName);
        request.setTitle("중복입니다.");
        request.setCategoryName("카테고리");

        mvc.perform(post("/video")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonMapper.toJson(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(fileName)))
                ;
    }

    @Test
    public void input_video_title_regular_expression_test() throws Exception {

        VideoInsertRequest request = new VideoInsertRequest();
        request.setTitle("제목.1124.231489123.coffee.mp4");
        request.setCategoryName("테스트");
        request.setFileName("제목.av.231489123 coffee.mp4");

        given(videoApiService.insert(any())).willReturn(Video.builder().build());

        mvc.perform(post("/video")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonMapper.toJson(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("SUCCESS")));
    }
}