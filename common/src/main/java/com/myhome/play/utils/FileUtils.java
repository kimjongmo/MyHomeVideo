package com.myhome.play.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;


@Component
public class FileUtils {

    @Value("${home.path}")
    private String HOME_PATH;

    /**
     * 카테고리에 존재하는 파일을 삭제한다.
     *
     * @param name     제거할 파일의 이름
     * @param category 제거할 파일의 카테고리
     * @return <code>true</code> 삭제; <code>false</code> 삭제 실패
     */
    public boolean delete(String category, String name){
        File file = getFile(category,name);
        if(file.exists())
            return file.delete();
        return true;
    }

    /**
     * 파일을 리턴한다.
     * */
    public File getFile(String category, String name){
        return new File(getAbsolutePath(category, name));
    }

    /**
     * 파일의 확장자 명을 가져온다.
     * */
    public String getExt(String name){
        int idx = name.lastIndexOf(".");
        if(idx==-1)
            return "";
        return name.substring(idx+1);
    }

    /**
     * 확장자를 제외한 파일의 이름을 리턴한다.
     * */
    public String getPureName(String name){
        int idx = name.lastIndexOf(".");
        if(idx == -1)
            return name;
        return name.substring(0,idx);
    }

    /**
     * 저장소에 저장된 파일의 절대 경로를 리턴한다.
     * */
    public String getAbsolutePath(String category, String name){
        return HOME_PATH+"/"+category+"/"+name;
    }

    /**
     * 파일을 생성한다.
     * */
    public boolean create(String category, String name){
        File file = getFile(category,name);
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
