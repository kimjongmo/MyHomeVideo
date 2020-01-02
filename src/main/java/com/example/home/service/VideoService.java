package com.example.home.service;

import com.example.home.components.MyResourceHttpRequestHandler;
import com.example.home.components.ThumbnailGenerator;
import com.example.home.components.VideoUtils;
import com.example.home.model.network.Header;
import com.example.home.model.network.request.VideoListRequest;
import com.example.home.model.network.response.VideoListResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VideoService {


    private VideoUtils videoUtils;
    private ThumbnailGenerator thumbnailGenerator;
    private MyResourceHttpRequestHandler handler;

    public VideoService(VideoUtils videoUtils,
                        ThumbnailGenerator thumbnailGenerator,
                        MyResourceHttpRequestHandler handler) {
        this.videoUtils = videoUtils;
        this.thumbnailGenerator = thumbnailGenerator;
        this.handler = handler;
    }

    public Header<List<VideoListResponse>> getList(VideoListRequest videoListRequest) {
        String category = videoListRequest.getCategory();
        List<File> fileList = videoUtils.getFileList(category);
        List<VideoListResponse> body = fileList.stream()
                .map((file) -> {
                    String pureFileName = videoUtils.getPureFileName(file);
                    File creatingImageFile = new File(thumbnailGenerator.getThumbnailPath() + "\\" + pureFileName + ".jpg");
                    thumbnailGenerator.extractImage(file, 60, creatingImageFile);
                    return makeResponse(file.getName());
                }).collect(Collectors.toList());
        return Header.OK(body);
    }

    public void getVideo(HttpServletRequest request,
                         HttpServletResponse response,
                         String fileName,
                         String category) throws ServletException, IOException {
        request.setAttribute("file", videoUtils.getFile(category, fileName));
        handler.handleRequest(request, response);
    }

    public VideoListResponse makeResponse(String fileName) {
        return VideoListResponse.builder()
                .name(fileName)
                .thumbnailUrl("/img/thumbnail/" + fileName.substring(0, fileName.lastIndexOf(".")) + ".jpg")
                .build();
    }
}
