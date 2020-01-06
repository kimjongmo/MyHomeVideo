package com.myhome.play.service;

import com.myhome.play.model.network.request.category.CategoryInsertRequest;
import com.myhome.play.model.network.request.video.VideoInsertRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@Service
public class RegisterApiService {

    @Value("${video.server.api}")
    private String videoServer;

    @Autowired
    private RestTemplateService restTemplateService;

    public ResponseEntity register(Object data, String name){
        URI uri = UriComponentsBuilder
                .newInstance()
                .fromHttpUrl(videoServer)
                .path(name)
                .encode().build().toUri();

        ParameterizedTypeReference<ResponseEntity> type = new ParameterizedTypeReference<ResponseEntity>() {};
        return restTemplateService.exchange(uri, HttpMethod.POST,data,type);
    }
}
