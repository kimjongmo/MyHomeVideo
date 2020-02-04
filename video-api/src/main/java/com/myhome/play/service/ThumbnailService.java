package com.myhome.play.service;

import com.myhome.play.components.ThumbnailGenerator;
import com.myhome.play.model.network.request.VideoListRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

@Service
@Slf4j
public class ThumbnailService {

    @Value("${thumbnail.path}")
    public String THUMBNAIL_PATH;
    private ThumbnailGenerator thumbnailGenerator;

    public ThumbnailService(ThumbnailGenerator thumbnailGenerator) {
        this.thumbnailGenerator = thumbnailGenerator;
    }

    public void create(File file) throws FileNotFoundException {
        String pureFileName = getFilePureName(file);
        File creatingImageFile = new File(THUMBNAIL_PATH + "\\" + pureFileName + ".jpg");
        thumbnailGenerator.extractImage(file, 60, creatingImageFile);
    }

    /**
     * 확장자를 제거한 파일의 순수 이름을 얻는다.
     * @date 2020.02.04
     * @param file 순수한 파일명을 얻고자 하는 대상 파일
     * @return 확장자를 제거한 파일의 이름
     * @author kimjongmo
     * @exception FileNotFoundException
     * */
    public String getFilePureName(File file) throws FileNotFoundException {
        if(file==null)
            throw new FileNotFoundException();
        String name = file.getName();
        return name.substring(0, name.lastIndexOf("."));
    }

}
