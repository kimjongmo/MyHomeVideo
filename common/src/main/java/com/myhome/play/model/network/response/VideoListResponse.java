package com.myhome.play.model.network.response;

import com.fasterxml.jackson.annotation.JsonProperty;
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
}
