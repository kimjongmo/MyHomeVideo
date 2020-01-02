package com.example.home.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Slf4j
public class PageController {

    @GetMapping("/")
    public String index(){
        log.info("index page");
        return "index";
    }

    @GetMapping("/player/{category}/{fileName}")
    public ModelAndView video(@PathVariable String category, @PathVariable String fileName) {
        log.info("category:{},fileName:{}",category,fileName);
        ModelAndView view = new ModelAndView("player");
        view.addObject("category", category);
        view.addObject("fileName", fileName);
        return view;
    }
}
