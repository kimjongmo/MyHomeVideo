package com.example.home.service;

import com.example.home.components.ThumbnailGenerator;
import com.example.home.components.VideoUtils;
import com.example.home.model.network.request.VideoListRequest;
import com.example.home.model.network.response.VideoListResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;


public class VideoServiceTest {

    private VideoService videoService;
    @Mock
    private VideoUtils videoUtils;
    @Mock
    private ThumbnailGenerator thumbnailGenerator;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        videoService = new VideoService(videoUtils, thumbnailGenerator);
    }

    @Test
    public void makeResponseTest() {
        String pureFileName = "런닝맨 1화";
        VideoListResponse response = videoService.makeResponse(pureFileName);
        assertEquals(response.getName(), pureFileName);
        assertEquals(response.getThumbnailUrl(), "/img/thumbnail/" + pureFileName + ".jpg");
    }

    @Test
    public void getListTest() {


        thumbnailGenerator.setThumbnailPath("C:\\Users\\KIM\\Desktop\\HomePlayer\\src\\main\\resources\\static\\img\\thumbnail");
        List<String> fileNameList = new ArrayList<>();

        for (int i = 0; i < 10; i++)
            fileNameList.add("런닝맨 " + i + "화.mp4");

        given(videoUtils.getList(any())).willReturn(fileNameList);

        List<VideoListResponse> data = videoService.getList(
                VideoListRequest.builder().category("런닝맨").build()).getData();
        data.stream().forEach(System.out::println);

        verify(videoUtils).getList("런닝맨");

    }

    @Test
    public void getListTestWithNotExistedCategory() {

    }

}