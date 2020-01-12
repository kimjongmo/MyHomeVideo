package com.myhome.play.service;

import com.myhome.play.model.network.Header;
import com.myhome.play.model.network.request.VideoListRequest;
import com.myhome.play.model.network.response.VideoListResponse;
import com.myhome.play.model.network.response.category.CategoryListResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.HttpHostConnectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.ConnectException;
import java.net.URI;
import java.util.List;

@Service
@Slf4j
public class PageApiService {

    @Value("${video.server.api}")
    private String videoServer;
    private ParameterizedTypeReference<Header<List<VideoListResponse>>> videoListResponseType
            = new ParameterizedTypeReference<Header<List<VideoListResponse>>>() {};

    private RestTemplateService restTemplateService;

    public PageApiService(RestTemplateService restTemplateService) {
        this.restTemplateService = restTemplateService;
    }


    public Header<List<VideoListResponse>> getContents(VideoListRequest videoListRequest) {

        URI uri = UriComponentsBuilder
                .newInstance()
                .fromHttpUrl(videoServer)
                .path("/video")
                .queryParam("category", videoListRequest.getCategory())
                .queryParam("page", videoListRequest.getPage())
                .encode().build().toUri();
        try {
            Header<List<VideoListResponse>> response = restTemplateService.exchange(uri, HttpMethod.GET, null, videoListResponseType);
            return response;
        }catch (ResourceAccessException e){
            return Header.ERROR("API 서버와 연결이 되지 않습니다.");
        }catch(Exception ex){
            log.error("[PageApiService][getContents] request = {}, error = {}",videoListRequest, ex.getMessage());
            return Header.ERROR("알 수 없는 오류...");
        }
    }

    public Header<List<CategoryListResponse>> getCategoryList() {

        URI uri = UriComponentsBuilder
                .newInstance()
                .fromHttpUrl(videoServer)
                .path("/category")
                .build().toUri();

        ParameterizedTypeReference<Header<List<CategoryListResponse>>> type
                = new ParameterizedTypeReference<Header<List<CategoryListResponse>>>() {
        };

        try {
            Header<List<CategoryListResponse>> response = restTemplateService.exchange(uri, HttpMethod.GET, null, type);
            return response;
        } catch (ResourceAccessException e) {
            return Header.ERROR("API 서버와 연결이 되지 않습니다.");
        } catch (Exception ex){
            log.info("[PageApiService][getCategoryList] error = {}",ex);
            return Header.ERROR("알 수 없는 오류...");
        }
    }

    public Header<List<VideoListResponse>> getRecentVideo(String category) {
        URI uri = UriComponentsBuilder
                .newInstance()
                .fromHttpUrl(videoServer)
                .path("/recentVideo")
                .queryParam("category", category)
                .encode().build().toUri();

        try {
            Header<List<VideoListResponse>> response = restTemplateService.exchange(uri, HttpMethod.GET, null, videoListResponseType);
            return response;
        }catch (ResourceAccessException e){
            return Header.ERROR("API 서버와 연결이 되지 않습니다.");
        }catch(Exception ex){
            log.error("[PageApiService][getContents] request = {}, error = {}",category, ex.getMessage());
            return Header.ERROR("알 수 없는 오류...");
        }
    }
}
