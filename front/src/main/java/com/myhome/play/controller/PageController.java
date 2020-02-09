package com.myhome.play.controller;

import com.myhome.play.model.entity.Video;
import com.myhome.play.model.network.Header;
import com.myhome.play.model.network.response.video.VideoInfoResponse;
import com.myhome.play.repo.VideoRepository;
import com.myhome.play.service.VideoApiService;
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
    @Autowired
    private VideoApiService videoApiService;

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

        Header<VideoInfoResponse> result = videoApiService.getInfo(id);

        //존재하지 않음.
        if(result.getStatus().equals("ERROR")){
            makePage(response);
            return null;
        }

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

}
