package com.myhome.play.exceptions;

public class FileDuplicateException extends RuntimeException{
    public FileDuplicateException(String fileName){
        super(fileName+"이 중복됩니다.");
    }
}
