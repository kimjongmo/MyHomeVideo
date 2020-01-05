package com.myhome.play.service;

import com.myhome.play.repo.CategoryRepository;
import com.myhome.play.repo.VideoRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;


public class VideoApiServiceTest {
    private VideoApiService videoApiService;

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private VideoRepository videoRepository;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        videoApiService = new VideoApiService(videoRepository,categoryRepository);
    }

    @Test
    public void getList(){

    }


}