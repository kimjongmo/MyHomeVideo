package com.myhome.play.model.network.response.category;

import com.myhome.play.model.entity.Category;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryModifyResponse {
    private Long id;
    private String name;

    public static CategoryModifyResponse of(Category category) {
        return CategoryModifyResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
