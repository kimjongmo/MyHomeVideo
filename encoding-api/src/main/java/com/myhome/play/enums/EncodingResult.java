package com.myhome.play.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EncodingResult {
    OK(0,"인코딩 성공"),
    INPUT_ERROR(1,"입력 데이터 오류"),
    ERROR(2,"인코딩 실패..."),
    SAVE_META_DATA_FAIL(3,"메타데이터 등록 실패")
    ;

    private int id;
    private String description;
}
