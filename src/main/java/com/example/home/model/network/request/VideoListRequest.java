package com.example.home.model.network.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoListRequest {
    @NotEmpty
    private String category;
}
