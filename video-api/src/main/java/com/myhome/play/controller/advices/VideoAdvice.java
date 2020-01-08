package com.myhome.play.controller.advices;

import com.myhome.play.exceptions.CategoryNotFoundException;
import com.myhome.play.exceptions.FileDuplicateException;
import com.myhome.play.model.network.Header;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class VideoAdvice {


    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({FileDuplicateException.class,CategoryNotFoundException.class})
    public Header videoError(RuntimeException e) {
        return Header.OK(e.getMessage());
    }
}
