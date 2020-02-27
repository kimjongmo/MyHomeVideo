package com.myhome.play.model.network.request.video;


import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VideoModifyRequest {
    private Long id;
    private String title;
    private String description;
    private String imgUrl;
    private Long views;
}
