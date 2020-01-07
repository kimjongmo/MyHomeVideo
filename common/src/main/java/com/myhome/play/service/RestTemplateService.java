package com.myhome.play.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class RestTemplateService {

    private RestTemplate restTemplate;

    public RestTemplateService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <T> T exchange(URI uri, HttpMethod httpMethod, Object requestData, ParameterizedTypeReference<T> type) {
        RequestEntity<Object> request = RequestEntity
                .method(httpMethod, uri)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(requestData);
        ResponseEntity<T> response = restTemplate.exchange(request, type);
        return response.getBody();
    }

}
