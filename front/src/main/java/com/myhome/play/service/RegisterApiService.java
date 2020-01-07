package com.myhome.play.service;

import com.myhome.play.model.network.request.category.CategoryInsertRequest;
import com.myhome.play.model.network.request.video.VideoInsertRequest;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class RegisterApiService {

    @Value("${video.server.api}")
    public String videoServer;

    @Autowired
    private RestTemplateService restTemplateService;

    public ResponseEntity register(Object data, String name){
        URI uri = UriComponentsBuilder
                .newInstance()
                .fromHttpUrl(videoServer)
                .path(name)
                .encode().build().toUri();

        ParameterizedTypeReference<ResponseEntity> type = new ParameterizedTypeReference<ResponseEntity>() {};
        try{
            ResponseEntity responseEntity = restTemplateService.exchange(uri, HttpMethod.POST,data,type);
            return responseEntity;
        } catch(Exception e){
            e.printStackTrace();
            log.error("{}:{}",e.getClass(),e.getMessage());
            return ResponseEntity.ok().body("알수없는 에러가 발생했습니다. 잠시 후 다시 시도해주세요.");
        }
    }
}
