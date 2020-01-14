package com.myhome.play.controller;

import com.myhome.play.service.FileUploadService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(FileUploadController.class)
public class FileUploadControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private FileUploadService fileUploadService;

    @Test
    public void other_media_type_request_test() throws Exception {
        mvc.perform(post("/upload")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("미디어 타입")));
    }

    @Test
    public void valid_data_request_test() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file",
                "filename.mp4",
                "multipart/form-data",
                "test".getBytes());


        mvc.perform(multipart("/upload")
                .file(file)
                .param("category_id","1")
                .param("title","제목"))
                .andExpect(status().isOk());
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

        mvc.perform(multipart("/upload")
                .file(file)
                .param("category_id","1")
                .param("title","제목","제목2"))
                .andExpect(status().isOk());
    }
}