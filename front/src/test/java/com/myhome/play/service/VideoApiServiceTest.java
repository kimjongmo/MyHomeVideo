package com.myhome.play.service;

import com.myhome.play.model.network.Header;
import com.myhome.play.model.network.response.video.VideoInfoResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

public class VideoApiServiceTest {
    private VideoApiService videoApiService;

    @Mock
    private RestTemplateService restTemplateService;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        videoApiService = new VideoApiService(restTemplateService);
        ReflectionTestUtils.setField(videoApiService, "videoServerIp", "http://192.168.35.239:9090");
    }

    @Test
    public void get_info_valid_data(){
        Long id = 1L;

        VideoInfoResponse response = VideoInfoResponse.builder()
                .id(id)
                .build();

        given(restTemplateService.exchange(any(),any(),any(),any()))
                .willReturn(Header.OK(response));

        Header<VideoInfoResponse> result = videoApiService.getInfo(id);
        assertEquals(result.getStatus(),"OK");
        assertEquals(result.getData().getId(),id);
    }

    @Test
    public void get_info_invalid_id_test(){
        Long id = 1L;

        given(restTemplateService.exchange(any(),any(),any(),any()))
                .willReturn(Header.ERROR("존재하지 않는 데이터"));

        Header<VideoInfoResponse> result = videoApiService.getInfo(id);

        assertEquals(result.getStatus(),"ERROR");
        assertEquals(result.getData(),null);
        assertEquals(result.getDescription(),"존재하지 않는 데이터");

    }
}