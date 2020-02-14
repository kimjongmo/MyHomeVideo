package com.myhome.play.service;

import com.myhome.play.components.ThumbnailGenerator;
import com.myhome.play.model.network.request.VideoListRequest;
import com.myhome.play.utils.FileUtils;
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
    private FileUtils fileUtils;

    public ThumbnailService(ThumbnailGenerator thumbnailGenerator,
                            FileUtils fileUtils) {
        this.thumbnailGenerator = thumbnailGenerator;
        this.fileUtils = fileUtils;
    }

    public boolean create(File file) {
        String pureFileName = fileUtils.getPureName(file.getName());
        File creatingImageFile = new File(THUMBNAIL_PATH + "\\" + pureFileName + ".jpg");
        try {
            thumbnailGenerator.extractImage(file, 60, creatingImageFile);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }


}
