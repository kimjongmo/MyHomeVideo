package com.myhome.play.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@Slf4j
public class PageController {
    @GetMapping("/")
    public ModelAndView index() {
        return new ModelAndView("index");
    }

    @GetMapping("/player/{category}/{fileName}")
    public ModelAndView play(HttpServletRequest request,
                             @PathVariable String category,
                             @PathVariable String fileName) {
        ModelAndView view = new ModelAndView("player");
        // TODO: 2020-01-04 나중에 리팩토링 할 것.
        String host = "http://"+request.getHeader("Host").split(":")[0]+":9090";
        log.info("host = {}",host);
        view.addObject("server",host);
        view.addObject("category", category);
        view.addObject("fileName", fileName);
        return view;
    }

}
