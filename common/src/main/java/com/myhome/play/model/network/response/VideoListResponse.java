package com.myhome.play.model.network.response;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoListResponse {
    private String name;
    private String thumbnailUrl;
}
