package com.example.home.controller;

import com.example.home.model.network.Header;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@CrossOrigin
public class CategoryController {

    @GetMapping("/category")
    public Header<List<String>> getList(){
        return Header.OK(Arrays.asList("런닝맨","한국영화"));
    }
}
