package com.myhome.play.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JsonMapper {

    private static ObjectMapper mapper = new ObjectMapper();

    public static String toJson(Object object){
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T strToObject(String message, Class<T> clazz){
        try {
            return mapper.readValue(message,clazz);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
