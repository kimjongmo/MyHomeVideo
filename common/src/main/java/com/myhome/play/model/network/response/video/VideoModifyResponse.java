package com.myhome.play.model.network.response.video;


import com.myhome.play.model.entity.Video;
import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VideoModifyResponse {
    private Long id;
    private String title;
    private String fileName;
    private String imgUrl;
    private Long views;

    public static VideoModifyResponse of(Video video){
        VideoModifyResponse response = new VideoModifyResponse();
        response.setId(video.getId());
        response.setFileName(video.getFileName());
        response.setImgUrl(video.getImgUrl());
        response.setTitle(video.getTitle());
        response.setViews(video.getViews());
        return response;
    }
}
