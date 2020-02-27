package com.myhome.play.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryInfoResponse {
    private Long id;
    private String name;
}
