package com.myhome.play.service;

import com.myhome.play.model.network.Header;
import com.myhome.play.model.network.request.VideoListRequest;
import com.myhome.play.model.network.response.VideoListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
public class PageApiService {

    @Value("${video.server.api}")
    private String videoServer;
    @Autowired
    private RestTemplateService restTemplateService;

    public Header<List<VideoListResponse>> getContents(VideoListRequest videoListRequest){

        URI uri = UriComponentsBuilder
                .newInstance()
                .fromHttpUrl(videoServer)
                .path("/video")
                .queryParam("category",videoListRequest.getCategory())
                .queryParam("page",videoListRequest.getPage())
                .encode().build().toUri();

        ParameterizedTypeReference<Header<List<VideoListResponse>>> type
                = new ParameterizedTypeReference<Header<List<VideoListResponse>>>() {};

        return restTemplateService.exchange(uri,HttpMethod.GET,null,type);
    }

    public Header<List<String>> getCategoryList() {

        URI uri = UriComponentsBuilder
                .newInstance()
                .fromHttpUrl(videoServer)
                .path("/category")
                .build().toUri();

        ParameterizedTypeReference<Header<List<String>>> type
                = new ParameterizedTypeReference<Header<List<String>>>() {};

        return restTemplateService.exchange(uri,HttpMethod.GET,null,type);
    }
}
