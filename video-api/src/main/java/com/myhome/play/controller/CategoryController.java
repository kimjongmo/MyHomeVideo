package com.myhome.play.controller;

import com.myhome.play.model.entity.Category;
import com.myhome.play.model.network.Header;
import com.myhome.play.model.network.request.category.CategoryInsertRequest;
import com.myhome.play.model.network.response.category.CategoryListResponse;
import com.myhome.play.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/category")
    public Header<List<CategoryListResponse>> getList() {
        return Header.OK(categoryService.getCategoryList());
    }

    @PostMapping("/category")
    public Header insert(@RequestBody @Valid CategoryInsertRequest request) {
        Category category = categoryService.insert(request);
        return Header.MESSAGE("SUCCESS");
    }
}
