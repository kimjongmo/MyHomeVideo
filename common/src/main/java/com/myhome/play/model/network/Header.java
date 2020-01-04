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

    public static <T> Header<T> OK(T body) {
        return (Header<T>) Header.builder()
                .status("OK")
                .data(body)
                .build();
    }

    public static Header NOT_FOUND(){
        return Header.builder()
                .status("NOT_FOUND")
                .build();
    }
}
