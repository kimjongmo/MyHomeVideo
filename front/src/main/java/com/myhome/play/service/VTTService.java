package com.myhome.play.service;

import com.myhome.play.components.MyResourceHttpRequestHandler;
import com.myhome.play.model.entity.Video;
import com.myhome.play.repo.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
public class VTTService {

    public String HOME_PATH = "D:\\MyHomeVideo";

    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private MyResourceHttpRequestHandler handler;

    public void getVTT(Long id, HttpServletResponse res, HttpServletRequest req) throws ServletException, IOException {
        Optional<Video> optionalVideo = videoRepository.findById(id);
        if(!optionalVideo.isPresent())
            return;

        Video video = optionalVideo.get();

        String vttName = video.getFileName().replaceAll(".mp4",".vtt");
        File file = new File(HOME_PATH + "/" + video.getCategory().getDirectoryPath() + "/" + vttName);
        if(!file.exists())
            return;
        req.setAttribute("file", file);
        handler.handleRequest(req, res);
    }

}
