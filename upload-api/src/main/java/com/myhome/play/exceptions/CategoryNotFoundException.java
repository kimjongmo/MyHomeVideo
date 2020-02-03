package com.myhome.play.exceptions;

public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException(String category){
        super(category+" 카테고리를 찾을 수 없습니다");
    }
}
