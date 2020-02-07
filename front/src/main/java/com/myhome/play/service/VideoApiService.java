package com.myhome.play.service;

import com.myhome.play.model.network.Header;
import com.myhome.play.model.network.response.video.VideoInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@Slf4j
public class VideoApiService {

    @Value("${video.server.api}")
    public String videoServerIp;

    private RestTemplateService restTemplateService;

    public VideoApiService(RestTemplateService restTemplateService){
        this.restTemplateService = restTemplateService;
    }

    public Header<VideoInfoResponse> getInfo(Long id){
        URI uri = UriComponentsBuilder
                .fromHttpUrl(videoServerIp)
                .path("/video/info")
                .queryParam("id",id)
                .build()
                .toUri();

        ParameterizedTypeReference<Header<VideoInfoResponse>> type =
                new ParameterizedTypeReference<Header<VideoInfoResponse>>() {};

        return restTemplateService.exchange(uri, HttpMethod.GET,null,type);
    }
}
