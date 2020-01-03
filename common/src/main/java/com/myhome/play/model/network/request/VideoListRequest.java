package com.myhome.play.model.network.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoListRequest {
    @NotEmpty
    private String category;
    @NotNull
    private Integer page;
}
