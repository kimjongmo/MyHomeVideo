package com.myhome.play.controller;

import com.myhome.play.model.entity.Video;
import com.myhome.play.repo.VideoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@Controller
@Slf4j
public class PageController {

    @Autowired
    private VideoRepository videoRepository;

    @GetMapping("/")
    public ModelAndView index() {
        return new ModelAndView("index");
    }

    @GetMapping("/{category}")
    public ModelAndView index2(@PathVariable String category) {
        return new ModelAndView("index");
    }

    @GetMapping("/player/{id}")
    public ModelAndView play(HttpServletRequest request, HttpServletResponse response, @PathVariable Long id) throws IOException {
        log.info("[/player/{}]", id);

        // TODO: 2020-01-27 여기서 db에 접근하지 않고, api 서버서버로부터 데이터 받아 확인하기
        // TODO: VTT 존재 여부, 비디오 존재 여부
        Optional<Video> optionalVideo = videoRepository.findById(id);

        if (!optionalVideo.isPresent()) {
            makePage(response);
            return null;
        }

        addViewCount(optionalVideo.get());

        ModelAndView view = new ModelAndView("player");
        String host = "http://" + request.getHeader("Host").split(":")[0];
        view.addObject("server", host + ":9090");
        view.addObject("id", id);
        return view;
    }

    @GetMapping("/register")
    public ModelAndView register() {
        return new ModelAndView("register");
    }

    private void makePage(HttpServletResponse response) throws IOException {
        PrintWriter writer = response.getWriter();
        writer.print("<script>alert('Sorry Not Existed Video...');history.back();</script>");
        writer.flush();
    }

    private void addViewCount(Video video) {
        Long views = video.getViews() + 1;
        video.setViews(views);
        Video saved = videoRepository.save(video);
    }
}
