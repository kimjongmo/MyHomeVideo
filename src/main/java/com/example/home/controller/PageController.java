package com.example.home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PageController {

    @GetMapping("pc")
    public String hello() {
        return "hello";
    }

    @GetMapping("/pages/{id}")
    public ModelAndView video(@PathVariable int id) {
        ModelAndView view = new ModelAndView("player");
        view.addObject("videoPath", "/video/" + id);
        return view;
    }
}
