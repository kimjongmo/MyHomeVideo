package com.myhome.play.controller;

import com.myhome.play.model.network.Header;
import com.myhome.play.service.RegisterApiService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(RegisterApiController.class)
public class RegisterApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RegisterApiService registerApiService;

    @Test
    public void video_valid_test_file_name_NULL() throws Exception {

        mvc.perform(post("/video")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"title\":\"제목\",\"category_name\":\"카테고리\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("\"field\":\"fileName\"")))
        ;
    }

    @Test
    public void video_valid_test_file_name_EMPTY() throws Exception {
        mvc.perform(post("/video")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"file_name\":\"\",\"title\":\"제목\",\"category_name\":\"카테고리\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("\"field\":\"fileName\"")))
                .andExpect(content().string(containsString("파일을 선택하세요")))
        ;
    }

    @Test
    public void video_valid_test__file_name_is_not_mp4() throws Exception {
        mvc.perform(post("/video")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"file_name\":\"534.jpg\",\"title\":\"제목\",\"category_name\":\"카테고리\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("\"field\":\"fileName\"")))
                .andExpect(content().string(containsString("mp4확장자만 업로드 가능합니다")))
        ;
    }

    @Test
    public void video_valid_test__file_name_is_mp4() throws Exception {
        given(registerApiService.register(any(), any())).willReturn(Header.OK("SUCCESS"));

        mvc.perform(post("/video")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"file_name\":\"534.mp4\",\"title\":\"제목\",\"category_name\":\"카테고리\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("SUCCESS")));
        ;
    }

    @Test
    public void video_valid_test_title_null() throws Exception {
        mvc.perform(post("/video")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"file_name\":\"534.jpg\",\"category_name\":\"카테고리\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("\"field\":\"title\"")))
                .andExpect(content().string(containsString("제목을 입력하세요")))
        ;
    }

    @Test
    public void video_valid_test_category_null() throws Exception {
        mvc.perform(post("/video")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"file_name\":\"534.jpg\",\"title\":\"제목\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("\"field\":\"categoryName\"")))
                .andExpect(content().string(containsString("카테고리 이름을 설정하세요")))
        ;
    }

    @Test
    public void category_valid_test_name_is_null() throws Exception {
        mvc.perform(post("/category")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"name\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("\"field\":\"name\"")))
                .andExpect(content().string(containsString("카테고리 이름을 설정해주세요")))
        ;
    }
}