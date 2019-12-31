package com.example.home.model.network;

import com.example.home.model.network.response.VideoListResponse;
import lombok.*;

import java.util.List;

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
}
