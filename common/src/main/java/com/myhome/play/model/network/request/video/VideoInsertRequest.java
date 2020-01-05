package com.myhome.play.model.network.request.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoInsertRequest {
    @JsonProperty("file_name")
    private String fileName;
    @JsonProperty("title")
    private String title;
    @JsonProperty("category_name")
    private String categoryName;
}
