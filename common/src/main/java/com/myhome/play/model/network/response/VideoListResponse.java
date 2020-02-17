package com.myhome.play.model.network.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.myhome.play.model.entity.Video;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoListResponse {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("thumbnail_url")
    private String thumbnailUrl;
    @JsonProperty("view")
    private Long view;
    @JsonProperty("description")
    private String description;

    public static VideoListResponse of(Video video){
        return VideoListResponse.builder()
                .id(video.getId())
                .title(video.getTitle())
                .thumbnailUrl(video.getImgUrl())
                .view(video.getViews())
                .description(video.getFileName())
                .build();
    }
}
