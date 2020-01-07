package com.myhome.play.exceptions;

public class CategoryDuplicateException extends RuntimeException {

    public CategoryDuplicateException(String categoryName){
        super(categoryName+"카테고리는 이미 존재합니다.");
    }
}
