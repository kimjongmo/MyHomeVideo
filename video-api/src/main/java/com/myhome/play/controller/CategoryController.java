package com.myhome.play.controller;

import com.myhome.play.model.entity.Category;
import com.myhome.play.model.network.Header;
import com.myhome.play.model.network.request.category.CategoryInsertRequest;
import com.myhome.play.model.network.response.category.CategoryListResponse;
import com.myhome.play.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
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
    public ResponseEntity insert(@RequestBody @Valid CategoryInsertRequest request) throws URISyntaxException {
        Category category = categoryService.insert(request);
        String uri = "/category/" +category.getId();
        return ResponseEntity.created(new URI(uri)).body("");
    }
}
