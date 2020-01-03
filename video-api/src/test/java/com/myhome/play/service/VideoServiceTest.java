package com.myhome.play.service;

import com.myhome.play.components.MyResourceHttpRequestHandler;
import com.myhome.play.components.ThumbnailGenerator;
import com.myhome.play.components.VideoUtils;
import com.myhome.play.model.network.request.VideoListRequest;
import com.myhome.play.model.network.response.VideoListResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


public class VideoServiceTest {

    private VideoService videoService;

    @Mock
    private VideoUtils videoUtils;
    @Mock
    private ThumbnailGenerator thumbnailGenerator;
    @Mock
    private MyResourceHttpRequestHandler handler;
    @Before

    public void setUp() {
        MockitoAnnotations.initMocks(this);
        videoService = new VideoService(videoUtils, thumbnailGenerator,handler);
        thumbnailGenerator.setThumbnailPath("C:\\Users\\KIM\\Desktop\\HomePlayer\\src\\main\\resources\\static\\img\\thumbnail");
    }

    @Test
    public void makeResponseTest() {
        String fileName = "런닝맨";
        String fileExt = ".mp4";
        VideoListResponse response = videoService.makeResponse(fileName+fileExt);
        assertEquals(response.getName(), fileName+fileExt);
        assertEquals(response.getThumbnailUrl(), "/img/thumbnail/" + fileName + ".jpg");
    }

    @Test
    public void getListTest() {

        List<File> fileNameList = new ArrayList<>();

        for (int i = 0; i < 10; i++)
            fileNameList.add(new File("D:\\MyHomeVideo\\RunningMan"+i+"Hwa.mp4"));

        given(videoUtils.getFileList(any(),anyInt())).willReturn(fileNameList);

        List<VideoListResponse> data = videoService.getList(VideoListRequest.builder().category("RunningMan").page(0).build()).getData();

        data.stream().forEach(System.out::println);

        verify(videoUtils).getFileList("RunningMan",0);

    }

    @Test
    public void pageTest() {
    }

}