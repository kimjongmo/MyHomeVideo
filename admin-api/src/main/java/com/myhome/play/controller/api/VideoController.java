package com.myhome.play.controller.api;

import com.myhome.play.model.network.Header;
import com.myhome.play.model.network.request.video.VideoModifyRequest;
import com.myhome.play.model.network.response.VideoListResponse;
import com.myhome.play.model.network.response.video.VideoInfoResponse;
import com.myhome.play.model.network.response.video.VideoModifyResponse;
import com.myhome.play.service.VideoApiService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class VideoController {

    private VideoApiService videoApiService;

    public VideoController(VideoApiService videoApiService){
        this.videoApiService = videoApiService;
    }

    @DeleteMapping(value = "/video/{id}")
    public Header delete(@PathVariable Long id){
        return videoApiService.delete(id);
    }

    @GetMapping("/video")
    public Header<List<VideoListResponse>> getList(@RequestParam String category, @PageableDefault(size = 6,page = 0) Pageable pageable) {
        return videoApiService.getList(category, pageable);
    }

    @GetMapping("/video/{id}")
    public Header<VideoInfoResponse> getInfo(@PathVariable Long id){
        return videoApiService.getInfo(id);
    }

    @PutMapping("/video")
    public Header<VideoModifyResponse> modify(VideoModifyRequest request){
        return videoApiService.modify(request);
    }
}


