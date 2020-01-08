package com.myhome.play.service;

import com.myhome.play.model.network.Header;
import com.myhome.play.model.network.request.category.CategoryInsertRequest;
import com.myhome.play.model.network.request.video.VideoInsertRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@Service
@Slf4j
public class RegisterApiService {

    @Value("${video.server.api}")
    public String videoServer;

    private RestTemplateService restTemplateService;

    public RegisterApiService(RestTemplateService restTemplateService) {
        this.restTemplateService = restTemplateService;
    }

    public Header register(Object data, String name){
        URI uri = UriComponentsBuilder
                .newInstance()
                .fromHttpUrl(videoServer)
                .path(name)
                .encode().build().toUri();

        ParameterizedTypeReference<Header> type = new ParameterizedTypeReference<Header>() {};
        try{
            Header response = restTemplateService.exchange(uri, HttpMethod.POST,data,type);
            return response;
        } catch(ResourceAccessException e){
            return Header.ERROR("API 서버와 연결이 되지 않습니다.");
        } catch(Exception e){
            log.error("{}",e);
            return Header.ERROR("알 수 없는 에러...");
        }
    }
}
