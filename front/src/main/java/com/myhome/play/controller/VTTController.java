package com.myhome.play.controller;

import com.myhome.play.service.VTTService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@Slf4j
@CrossOrigin
public class VTTController {

    @Autowired
    private VTTService vttService;

    @GetMapping("/vtt/{id}")
    public void getVTT(@PathVariable Long id, HttpServletResponse res, HttpServletRequest req) throws ServletException, IOException {
        vttService.getVTT(id,res,req);
    }

}
