package com.myhome.play.model.network.request.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryInsertRequest {
    @NotEmpty(message = "카테고리 이름을 설정해주세요")
    private String name;

}
