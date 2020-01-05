package com.myhome.play.controller;

import com.myhome.play.service.VideoApiService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@WebMvcTest
public class VideoApiControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private VideoApiService videoApiService;

    @Test
    public void getList(){

    }
}