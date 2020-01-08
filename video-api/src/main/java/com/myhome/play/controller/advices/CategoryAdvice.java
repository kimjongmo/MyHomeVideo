package com.myhome.play.controller.advices;

import com.myhome.play.exceptions.CategoryDuplicateException;
import com.myhome.play.model.network.Header;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CategoryAdvice {
    @ExceptionHandler(CategoryDuplicateException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Header duplicate(RuntimeException e){
        return Header.OK(e.getMessage());
    }

}
