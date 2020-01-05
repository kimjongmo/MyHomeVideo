package com.myhome.play.service;

import com.myhome.play.components.ThumbnailGenerator;
import com.myhome.play.components.VideoUtils;
import com.myhome.play.model.network.request.VideoListRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
@Slf4j
public class ThumbnailService {


    private VideoUtils videoUtils;
    private ThumbnailGenerator thumbnailGenerator;

    public ThumbnailService(VideoUtils videoUtils,
                            ThumbnailGenerator thumbnailGenerator) {
        this.videoUtils = videoUtils;
        this.thumbnailGenerator = thumbnailGenerator;
    }

    public void create(File file){
        String pureFileName = videoUtils.getPureFileName(file);
        File creatingImageFile = new File(thumbnailGenerator.getThumbnailPath() + "\\" + pureFileName + ".jpg");
        thumbnailGenerator.extractImage(file, 60, creatingImageFile);
    }
}
