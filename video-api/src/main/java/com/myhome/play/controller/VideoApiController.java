package com.myhome.play.controller;


import com.myhome.play.model.entity.Video;
import com.myhome.play.model.network.Header;
import com.myhome.play.model.network.request.video.VideoInsertRequest;
import com.myhome.play.model.network.response.VideoListResponse;
import com.myhome.play.service.VideoApiService;
import com.myhome.play.service.ThumbnailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Slf4j
@RestController
public class VideoApiController {

    private VideoApiService videoApiService;
    private ThumbnailService thumbnailService;

    public VideoApiController(VideoApiService videoApiService, ThumbnailService thumbnailService) {
        this.videoApiService = videoApiService;
        this.thumbnailService = thumbnailService;
    }

    @GetMapping("/video")
    public Header<List<VideoListResponse>> getList(@RequestParam String category, @PageableDefault(size = 6,page = 0) Pageable pageable) {
        log.info("[GET /video] category={},page={}",category,pageable.getPageNumber());
        return videoApiService.getList(category, pageable);
    }

    @GetMapping("/video/{id}")
    public void play(HttpServletRequest req,
                     HttpServletResponse res,
                     @PathVariable Long id) throws IOException, ServletException {
        log.info("[GET /video/{}]",id);
        videoApiService.play(req, res, id);
    }

    @PostMapping("/video")
    public Header<Video> insert(@RequestBody @Valid VideoInsertRequest videoInsertRequest){
        Video video = videoApiService.insert(videoInsertRequest);
        return Header.OK("SUCCESS");
    }

    @GetMapping("/recentVideo")
    public Header<List<VideoListResponse>> recentRegisteredVideo(@RequestParam(required = false) String category){
        return videoApiService.getRecentRegistered(category);
    }
}
