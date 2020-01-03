package com.myhome.play.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PageController {
    @GetMapping("/")
    public ModelAndView index() {
        return new ModelAndView("index");
    }

    @GetMapping("/player/{category}/{fileName}")
    public ModelAndView play(@PathVariable String category,
                             @PathVariable String fileName){
        ModelAndView view =  new ModelAndView("player");
        view.addObject("category",category);
        view.addObject("fileName",fileName);
        return view;
    }

}
