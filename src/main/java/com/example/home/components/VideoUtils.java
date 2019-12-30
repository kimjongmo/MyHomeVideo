package com.example.home.components;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class VideoUtils {
    private final String HOME_PATH = "D:\\런닝맨\\런닝맨 2016";

    public List<String> getList(){
        log.info("search path : {}",HOME_PATH);
        File file = new File(HOME_PATH);
        return Arrays.asList(file.list());
    }

    public File getFile(int id){
        String fileName = getList().get(id);
        log.info("search file id : {}",HOME_PATH+"\\"+fileName);
        File file = new File(HOME_PATH+"\\"+fileName);
        return file;
    }

}
