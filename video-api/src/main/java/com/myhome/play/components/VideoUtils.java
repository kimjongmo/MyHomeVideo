package com.myhome.play.components;

import com.myhome.play.exceptions.CategoryNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class VideoUtils {

    @Value("${home.path}")
    public String HOME_PATH;
    private int perCountOfPage = 6;

    @PostConstruct
    public void setUp() {
        File file = new File(HOME_PATH);
        if (!file.exists()) {
            log.info("{} is not exist. Making folder ...", HOME_PATH);
            file.mkdirs();
            log.info("Success make to folder");
        }
    }

    // 파일 형식 && mp4 확장자만 필터링
//    public List<File> getFileList(String category) {
//        String path = HOME_PATH + "\\" + category;
//        log.info("search path : {}", path);
//        File file = new File(path);
//
//        if (!file.exists())
//            throw new CategoryNotFoundException(category);
//        return Arrays.asList(file.listFiles(pathname -> {
//            if (pathname.isFile() && getFileExt(pathname).equals("mp4"))
//                return true;
//            return false;
//        }));
//    }

    public List<File> getFileList(String category, int page){
        String path = HOME_PATH + "\\" + category;
        log.info("search path : {}", path);
        File file = new File(path);

        if (!file.exists())
            throw new CategoryNotFoundException(category);

        File[] filteredFileList = file.listFiles(pathname -> {
            if (pathname.isFile() && getFileExt(pathname).equals("mp4"))
                return true;
            return false;
        });

        if(filteredFileList.length <= perCountOfPage * page){
            return Collections.emptyList();
        }

        if(filteredFileList.length < perCountOfPage * (page + 1)){
            return Arrays.asList(filteredFileList).subList(perCountOfPage * page,filteredFileList.length);
        }

        return Arrays.asList(filteredFileList).subList(perCountOfPage * page, perCountOfPage * (page + 1));
    }

    public File getFile(String category, String fileName) throws FileNotFoundException {
        String filePath = HOME_PATH + "\\" + category + "\\" + fileName;
        File file = new File(filePath);
        if (!file.exists())
            throw new FileNotFoundException();
        return file;
    }

    public String getPureFileName(File file) {
        String name = file.getName();
        return name.substring(0, name.lastIndexOf("."));
    }

    public String getFileExt(File file) {
        String name = file.getName();
        return name.substring(name.lastIndexOf('.') + 1);
    }


}
