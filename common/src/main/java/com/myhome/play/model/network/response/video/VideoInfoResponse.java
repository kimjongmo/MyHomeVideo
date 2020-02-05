package com.myhome.play.model.network.response.video;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class VideoInfoResponse {
    private Long id;
    private String title;
    private String fileName;
    private String imgUrl;
    private Long views;
    private String categoryName;
}

