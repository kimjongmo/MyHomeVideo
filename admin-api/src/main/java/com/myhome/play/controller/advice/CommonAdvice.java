package com.myhome.play.controller.advice;

import com.myhome.play.model.network.Header;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.net.BindException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class CommonAdvice {
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Header bindException(MethodArgumentNotValidException e){
        List<ErrorResult> errorResultList =
                e.getBindingResult()
                        .getFieldErrors().stream()
                        .map(ErrorResult::new)
                        .collect(Collectors.toList());
        return Header.OK(errorResultList);
    }

    @ToString
    @Getter
    @Setter
    @NoArgsConstructor
    class ErrorResult{
        private String field;
        private String defaultMessage;
        public ErrorResult(FieldError fieldError){
            this.field = fieldError.getField();
            this.defaultMessage = fieldError.getDefaultMessage();
        }
    }

}
