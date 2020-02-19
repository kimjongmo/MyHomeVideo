package com.myhome.play.model.network.request.category;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryModifyRequest {
    @NotNull(message = "id값은 필수입니다.")
    private Long id;    //카테고리 ID
    @NotEmpty(message = "name값은 필수입니다.")
    private String name;    //변경할 이름
}
