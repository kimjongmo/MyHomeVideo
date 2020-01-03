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
    @JsonProperty("name")
    private String name;
    @JsonProperty("thumbnail_url")
    private String thumbnailUrl;
}
