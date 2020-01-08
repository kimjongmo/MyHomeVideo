package com.myhome.play.service;

import com.myhome.play.model.network.Header;
import com.myhome.play.model.network.request.category.CategoryInsertRequest;
import com.myhome.play.model.network.request.video.VideoInsertRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.ResourceAccessException;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

public class RegisterApiServiceTest {

    @Mock
    private RestTemplateService restTemplateService;

    private RegisterApiService registerApiService;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        registerApiService = new RegisterApiService(restTemplateService);
        ReflectionTestUtils.setField(registerApiService, "videoServer", "http://localhost:9090");
    }

    @Test
    public void 카테고리_데이터_등록_API_연결이_되지않을때_테스트() {
        given(restTemplateService.exchange(any(), eq(HttpMethod.POST),any(),any())).willThrow(new ResourceAccessException(""));

        CategoryInsertRequest category = new CategoryInsertRequest();
        category.setName("런닝맨");

        Header response = registerApiService.register(category,"category");

        assertNull(response.getData());
        assertTrue(response.getStatus().equals("ERROR"));
        assertTrue(response.getDescription().equals("API 서버와 연결이 되지 않습니다."));
    }
    @Test
    public void 비디오_데이터_등록_API_연결이_되지않을때_테스트() {
        given(restTemplateService.exchange(any(), eq(HttpMethod.POST),any(),any())).willThrow(new ResourceAccessException(""));

        VideoInsertRequest video = new VideoInsertRequest();

        video.setCategoryName("런닝맨");
        video.setFileName("런닝맨 n화.mp4");
        video.setTitle("런닝맨 제목");

        Header response = registerApiService.register(video,"video");

        assertNull(response.getData());
        assertTrue(response.getStatus().equals("ERROR"));
        assertTrue(response.getDescription().equals("API 서버와 연결이 되지 않습니다."));
    }

    @Test
    public void 카테고리_등록_API_정상_연결_테스트() {
        String category = "런닝맨";
        given(restTemplateService.exchange(any(), eq(HttpMethod.POST),any(),any()))
                .willReturn(Header.OK("SUCCESS"));

        CategoryInsertRequest request = new CategoryInsertRequest();
        request.setName(category);

        Header response = registerApiService.register(request,"category");
        assertTrue(response.getStatus().equals("OK"));
        assertTrue(response.getDescription().equals("SUCCESS"));
    }

    @Test
    public void 비디오_등록_API_정상_연결_테스트() {
        given(restTemplateService.exchange(any(), eq(HttpMethod.POST),any(),any()))
                .willReturn(Header.OK("SUCCESS"));

        VideoInsertRequest video = new VideoInsertRequest();

        video.setCategoryName("런닝맨");
        video.setFileName("런닝맨 n화.mp4");
        video.setTitle("런닝맨 제목");

        Header response = registerApiService.register(video,"video");
        assertTrue(response.getStatus().equals("OK"));
        assertTrue(response.getDescription().equals("SUCCESS"));
    }

    @Test
    public void 카테고리_중복일_때(){

        String category = "런닝맨";
        given(restTemplateService.exchange(any(), eq(HttpMethod.POST),any(),any()))
                .willReturn(Header.OK(category+" 카테고리는 이미 존재합니다."));

        CategoryInsertRequest request = new CategoryInsertRequest();
        request.setName(category);

        Header response = registerApiService.register(request,"category");
        assertTrue(response.getStatus().equals("OK"));
        assertTrue(response.getDescription().contains(category));
    }
    @Test
    public void 비디오_중복일_때(){
        String fileName = "test.mp4";
        given(restTemplateService.exchange(any(), eq(HttpMethod.POST),any(),any()))
                .willReturn(Header.OK(fileName+"이 중복됩니다."));

        VideoInsertRequest video = new VideoInsertRequest();

        video.setCategoryName("런닝맨");
        video.setFileName(fileName);
        video.setTitle("런닝맨 제목");

        Header response = registerApiService.register(video,"video");
        assertTrue(response.getStatus().equals("OK"));
        assertTrue(response.getDescription().contains(fileName));
    }

    @Test
    public void 비디오_생성시_없는_카테고리(){
        String categoryName = "TEST";
        given(restTemplateService.exchange(any(), eq(HttpMethod.POST),any(),any()))
                .willReturn(Header.OK(categoryName+" 카테고리를 찾을 수 없습니다"));

        VideoInsertRequest video = new VideoInsertRequest();

        video.setCategoryName(categoryName);
        video.setFileName("test");
        video.setTitle("런닝맨 제목");

        Header response = registerApiService.register(video,"video");
        assertTrue(response.getStatus().equals("OK"));
        assertTrue(response.getDescription().contains(categoryName));
    }

//    @Test
//    public void 존재하지_않는_URL() {
//    }
}