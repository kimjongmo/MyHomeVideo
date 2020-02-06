package com.myhome.play.model.network;

import lombok.*;


@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Header<T> {

    private String status;
    private T data;
    private String description;

    public static <T> Header OK(T body) {
        return Header.builder()
                .status("OK")
                .data(body)
                .build();
    }

    public static Header MESSAGE(String description) {
        return Header.builder()
                .status("OK")
                .description(description)
                .build();
    }

    public static Header ERROR(String description){
        return Header.builder()
                .status("ERROR")
                .description(description)
                .build();
    }
}
