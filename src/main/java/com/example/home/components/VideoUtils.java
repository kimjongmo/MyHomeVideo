package com.example.home.components;

import com.example.home.exceptions.CategoryNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class VideoUtils {

    @Value("${home.path}")
    private String HOME_PATH;

    @PostConstruct
    public void setUp() {
        File file = new File(HOME_PATH);
        if (!file.exists()) {
            log.info("{} is not exist. Making folder ...", HOME_PATH);
            file.mkdirs();
            log.info("Success make to folder");
        }
    }

    public List<String> getList(String category) {
        String path = HOME_PATH + "\\" + category;
        log.info("search path : {}", path);
        File file = new File(path);
        // TODO: 2019-12-31 만약 폴더가 없다면 에러 발생시키기.
        if(!file.exists())
            throw new CategoryNotFoundException(category);
        return Arrays.asList(file.list());
    }

    public File getFile(String category, String fileName) {
        String filePath = HOME_PATH + "\\" + category + "\\" + fileName;
        File file = new File(filePath);
        // TODO: 2019-12-31 만약 파일이 없다면 에러 발생시키기.
        return file;
    }

}
