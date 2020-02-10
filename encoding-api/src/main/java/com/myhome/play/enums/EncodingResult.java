package com.myhome.play.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EncodingResult {
    OK(0,"인코딩 성공"),
    INPUT_ERROR(1,"입력 데이터 오류"),
    ERROR(2,"알수없는 오류")
    ;

    private int id;
    private String description;
}
