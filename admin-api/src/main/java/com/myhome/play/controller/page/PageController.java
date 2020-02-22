package com.myhome.play.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PageController {

    @GetMapping("/")
    public ModelAndView index(){
        return new ModelAndView("index");
    }

    @GetMapping("/videoPage")
    public ModelAndView videoPage(){
        return new ModelAndView("video");
    }

    @GetMapping("/categoryPage")
    public ModelAndView categoryPage(){
        return new ModelAndView("category");
    }
}
