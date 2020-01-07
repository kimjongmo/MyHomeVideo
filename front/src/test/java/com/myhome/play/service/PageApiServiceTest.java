package com.myhome.play.service;

import com.myhome.play.model.network.Header;
import com.myhome.play.model.network.request.VideoListRequest;
import com.myhome.play.model.network.response.VideoListResponse;
import com.myhome.play.model.network.response.category.CategoryListResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.ResourceAccessException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

public class PageApiServiceTest {

    @Mock
    private RestTemplateService restTemplateService;

    private PageApiService pageApiService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        pageApiService = new PageApiService(restTemplateService);
        ReflectionTestUtils.setField(pageApiService, "videoServer", "http://localhost:9090");
    }

    @Test
    public void 카테고리리스트_API_연결이_되지않을때_테스트() {
        given(restTemplateService.exchange(any(), eq(HttpMethod.GET), eq(null), any())).willThrow(new ResourceAccessException(""));

        Header<List<CategoryListResponse>> response = pageApiService.getCategoryList();

        assertNull(response.getData());
        assertTrue(response.getStatus().equals("ERROR"));
        assertTrue(response.getDescription().equals("API 서버와 연결이 되지 않습니다."));
    }

    @Test
    public void 카테고리리스트_API_정상_연결_테스트() {
        List<CategoryListResponse> listResponses = Arrays.asList(
                CategoryListResponse.builder().id(1L).name("무한도전").build(),
                CategoryListResponse.builder().id(2L).name("런닝맨").build());

        given(restTemplateService.exchange(any(), eq(HttpMethod.GET), eq(null), any()))
                .willReturn(Header.OK(listResponses));

        Header<List<CategoryListResponse>> response = pageApiService.getCategoryList();

        assertTrue(response.getStatus().equals("OK"));
        assertNotNull(response.getData());
        List<CategoryListResponse> data = response.getData();
        assertTrue(data.get(0).getName().equals("무한도전"));
        assertTrue(data.get(1).getName().equals("런닝맨"));
    }

    @Test
    public void 비디오리스트_API_연결이_되지않을때_테스트() {
        given(restTemplateService.exchange(any(), eq(HttpMethod.GET), eq(null), any())).willThrow(new ResourceAccessException(""));

        VideoListRequest videoListRequest = VideoListRequest.builder().category("런닝맨").page(0).build();
        Header<List<VideoListResponse>> response = pageApiService.getContents(videoListRequest);

        assertNull(response.getData());
        assertTrue(response.getStatus().equals("ERROR"));
        assertTrue(response.getDescription().equals("API 서버와 연결이 되지 않습니다."));
    }

    @Test
    public void 비디오리스트_API_정상_연결_테스트() {
        List<VideoListResponse> responseData = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            VideoListResponse data = VideoListResponse.builder()
                    .id(i*1L)
                    .title("런닝맨"+i)
                    .description("런닝맨"+i+"화.mp4")
                    .thumbnailUrl("/img/thumbnail/런닝맨"+i+"화.mp4")
                    .view(0L)
                    .build();
            responseData.add(data);
        }
        given(restTemplateService.exchange(any(), eq(HttpMethod.GET), eq(null), any()))
                .willReturn(Header.OK(responseData));

        VideoListRequest videoListRequest = VideoListRequest.builder().category("런닝맨").page(0).build();
        Header<List<VideoListResponse>> response = pageApiService.getContents(videoListRequest);

        assertTrue(response.getStatus().equals("OK"));
        assertNotNull(response.getData());
        assertTrue(response.getData().size()==5);
        assertTrue(response.getData().get(3).getTitle().equals("런닝맨3"));
    }
}