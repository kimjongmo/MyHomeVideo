package com.myhome.play.controller;

import com.myhome.play.model.network.Header;
import com.myhome.play.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/category")
   public Header<List<String>> getList(){
       return Header.OK(categoryService.getCategoryList());
   }
}
