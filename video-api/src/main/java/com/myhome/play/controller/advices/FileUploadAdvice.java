package com.myhome.play.controller.advices;

import com.myhome.play.exceptions.DataSizeNotMatchException;
import com.myhome.play.model.network.Header;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class FileUploadAdvice {
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Header mediaTypeError(){
        return Header.ERROR("미디어 타입이 올바르지 않습니다.");
    }

    @ExceptionHandler(DataSizeNotMatchException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Header dataSizeError(RuntimeException e){
        return Header.OK(e.getMessage());
    }
}
