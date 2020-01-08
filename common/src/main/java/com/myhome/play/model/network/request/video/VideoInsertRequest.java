package com.myhome.play.model.network.request.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoInsertRequest {

    @NotEmpty(message = "파일을 선택하세요")
    @Pattern(regexp = "^([\\S]+(\\.(?i)(mp4))$)",message = "mp4확장자만 업로드 가능합니다")
    @JsonProperty("file_name")
    private String fileName;

    @JsonProperty("title")
    @NotEmpty(message = "제목을 입력하세요")
    private String title;

    @NotEmpty(message = "카테고리 이름을 설정하세요")
    @JsonProperty("category_name")
    private String categoryName;
}
