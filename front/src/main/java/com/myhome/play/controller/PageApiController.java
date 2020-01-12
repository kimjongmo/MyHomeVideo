package com.myhome.play.controller;

import com.myhome.play.model.network.Header;
import com.myhome.play.model.network.request.VideoListRequest;
import com.myhome.play.model.network.response.VideoListResponse;
import com.myhome.play.model.network.response.category.CategoryListResponse;
import com.myhome.play.service.PageApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class PageApiController {

    @Autowired
    private PageApiService pageApiService;

    @GetMapping("/contents")
    public Header<List<VideoListResponse>> getContent(@Valid VideoListRequest videoListRequest){
        log.info("[/contents] category = {}, page={}",videoListRequest.getCategory(),videoListRequest.getPage());
        return pageApiService.getContents(videoListRequest);
    }

    @GetMapping("/category")
    public Header<List<CategoryListResponse>> getCategory(){
        log.info("[/category]");
        return pageApiService.getCategoryList();
    }

    @GetMapping("/recentVideo")
    public Header<List<VideoListResponse>> getRecentVideo(@RequestParam(required = false) String category){
        return pageApiService.getRecentVideo(category);
    }

}
