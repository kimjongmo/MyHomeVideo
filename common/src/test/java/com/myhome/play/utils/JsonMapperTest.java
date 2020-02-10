package com.myhome.play.utils;

import com.myhome.play.model.network.request.encode.EncodeRequestDTO;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonMapperTest {

    @Test
    public void string_to_object_test() {
        String message = "{\"name\":\"kim\",\"category\":\"test\"}";
        EncodeRequestDTO dto =
                JsonMapper.strToObject(message,EncodeRequestDTO.class);

        assertTrue(dto.getCategory().equals("test"));
        assertTrue(dto.getName().equals("kim"));
    }

    @Test
    public void object_to_json(){
        EncodeRequestDTO dto = EncodeRequestDTO.builder()
                .name("kim")
                .category("test")
                .build();

        String message = JsonMapper.toJson(dto);
        assertTrue(message.contains("kim"));
        assertTrue(message.contains("test"));
    }
}