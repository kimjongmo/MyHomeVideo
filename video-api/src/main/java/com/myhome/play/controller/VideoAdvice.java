package com.myhome.play.controller;

import com.myhome.play.exceptions.CategoryNotFoundException;
import com.myhome.play.exceptions.FileDuplicateException;
import com.myhome.play.model.network.Header;
import org.omg.SendingContext.RunTime;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ControllerAdvice
public class VideoAdvice {


    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({FileDuplicateException.class,
            CategoryNotFoundException.class,})
    public Header videoError(RuntimeException e) {
        return Header.OK(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.OK)
    public Header requestParamError(Exception e) {
        List<String> params = getRequestParamInError(e.getMessage());
        StringBuilder message = new StringBuilder("파라미터");

        message.append(params.toString() + "가 요구됩니다.");

        return Header.OK(message.toString());
    }

    private List<String> getRequestParamInError(String message) {
        List<String> list = new ArrayList<>();
        String[] words = message.split("\\'");
        int i = 1;
        while (i < words.length) {
            list.add(words[i]);
            i += 2;
        }
        return list;
    }

}
