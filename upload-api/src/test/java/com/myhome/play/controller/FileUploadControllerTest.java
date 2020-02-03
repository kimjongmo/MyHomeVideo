package com.myhome.play.controller;

import com.myhome.play.exceptions.CategoryNotFoundException;
import com.myhome.play.exceptions.DataSizeNotMatchException;
import com.myhome.play.serivce.FileUploadService;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@WebMvcTest(FileUploadController.class)
public class FileUploadControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private FileUploadService fileUploadService;

    @Test
    public void other_media_type_request_test() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/upload")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("미디어 타입")));
    }

    @Test
    public void valid_data_request_test() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file",
                "filename.mp4",
                "multipart/form-data",
                "test".getBytes());


        mvc.perform(MockMvcRequestBuilders.multipart("/upload")
                .file(file)
                .param("category_id", "1")
                .param("title", "제목"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void multi_valid_data_request_test() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file",
                "filename.mp4",
                "multipart/form-data",
                "test".getBytes());

        MockMultipartFile file1 = new MockMultipartFile("file",
                "filename2.mp4",
                "multipart/form-data",
                "test".getBytes());

        mvc.perform(MockMvcRequestBuilders.multipart("/upload")
                .file(file)
                .param("category_id", "1")
                .param("title", "제목", "제목2"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void invalid_category_id_request_test() throws Exception {

        BDDMockito.given(fileUploadService.upload(ArgumentMatchers.anyList(), ArgumentMatchers.anyLong(), ArgumentMatchers.anyList()))
                .willThrow(new CategoryNotFoundException("카테고리를 찾을 수 없습니다"));

        MockMultipartFile file = new MockMultipartFile("file",
                "filename.mp4",
                "multipart/form-data",
                "test".getBytes());

        mvc.perform(MockMvcRequestBuilders.multipart("/upload")
                .file(file)
                .param("category_id", "1")
                .param("title", "제목"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("카테고리")));
    }

    @Test
    public void multi_invalid_data_size_request_test() throws Exception {

        BDDMockito.given(fileUploadService.upload(ArgumentMatchers.anyList(), ArgumentMatchers.anyLong(), ArgumentMatchers.anyList()))
                .willThrow(new DataSizeNotMatchException("파일에는 제목이 꼭 필요합니다"));

        MockMultipartFile file = new MockMultipartFile("file",
                "filename.mp4",
                "multipart/form-data",
                "test".getBytes());

        MockMultipartFile file1 = new MockMultipartFile("file",
                "filename2.mp4",
                "multipart/form-data",
                "test".getBytes());

        mvc.perform(MockMvcRequestBuilders.multipart("/upload")
                .file(file)
                .param("category_id", "1")
                .param("title","제목1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("파일에는 제목이")));
    }

}