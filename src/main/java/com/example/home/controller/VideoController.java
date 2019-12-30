package com.example.home.controller;

import com.example.home.components.MyResourceHttpRequestHandler;
import com.example.home.components.VideoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/video")
public class VideoController {

    @Autowired
    private VideoUtils videoUtils;
    @Autowired
    private MyResourceHttpRequestHandler handler;

    @GetMapping("/list")
    public List<String> list() {
        return videoUtils.getList();
    }

    @GetMapping("/{id}")
    public void play(HttpServletRequest req, HttpServletResponse res ,@PathVariable("id") int id) throws IOException, ServletException {
        req.setAttribute("file",videoUtils.getFile(id));
        handler.handleRequest(req,res);
    }


}
