package com.myhome.play.model.network.request.encode;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class EncodeRequestDTO {
    private String name;
    private String category;
}
