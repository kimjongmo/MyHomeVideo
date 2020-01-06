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
    public ModelAndView index2(@PathVariable String category){
        return new ModelAndView("index");
    }

    @GetMapping("/player/{id}")
    public ModelAndView play(HttpServletRequest request, @PathVariable Long id) {
        log.info("[/player/{}]",id);
        Optional<Video> optionalVideo = videoRepository.findById(id);
        log.info("{}",optionalVideo.get());
        if(optionalVideo.isPresent()){
            Video video = optionalVideo.get();
            Long views = video.getViews()+1;
            video.setViews(views);
            Video saved = videoRepository.save(video);
            log.info("views = {}",saved.getViews());
        }

        ModelAndView view = new ModelAndView("player");
        // TODO: 2020-01-04 나중에 리팩토링 할 것.
        String host = "http://"+request.getHeader("Host").split(":")[0];
        log.info("host = {}",host);
        view.addObject("server",host+":9090");
        view.addObject("id", id);
        return view;
    }

    @GetMapping("/register")
    public ModelAndView register(){
        return new ModelAndView("register");
    }
}
