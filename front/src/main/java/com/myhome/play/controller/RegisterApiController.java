package com.myhome.play.controller;

import com.myhome.play.model.network.Header;
import com.myhome.play.model.network.request.category.CategoryInsertRequest;
import com.myhome.play.model.network.request.video.VideoInsertRequest;
import com.myhome.play.service.RegisterApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Slf4j
public class RegisterApiController {

    @Autowired
    private RegisterApiService registerApiService;

    @PostMapping("/video")
    public Header registerVideo(@RequestBody @Valid VideoInsertRequest videoInsertRequest){
        log.info("video register data : {}",videoInsertRequest);
        return registerApiService.register(videoInsertRequest,"video");
    }

    @PostMapping("/category")
    public Header registerCategory(@RequestBody @Valid CategoryInsertRequest categoryInsertRequest){
        log.info("category register data : {}",categoryInsertRequest);
        return registerApiService.register(categoryInsertRequest,"category");
    }
}
