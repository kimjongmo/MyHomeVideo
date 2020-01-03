package com.myhome.play.controller;

import com.myhome.play.model.network.Header;
import com.myhome.play.model.network.request.VideoListRequest;
import com.myhome.play.model.network.response.VideoListResponse;
import com.myhome.play.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @GetMapping
    public Header<List<VideoListResponse>> list(@Valid VideoListRequest videoListRequest) {
        log.info("category = {}, page={}",videoListRequest.getCategory(),videoListRequest.getPage());
        return videoService.getList(videoListRequest);
    }

    @GetMapping("/{category}/{fileName}")
    public void play(HttpServletRequest req,
                     HttpServletResponse res,
                     @PathVariable String fileName,
                     @PathVariable String category) throws IOException, ServletException {
        videoService.getVideo(req,res,fileName,category);
    }


}
