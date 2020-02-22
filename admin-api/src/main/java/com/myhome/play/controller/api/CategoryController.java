package com.myhome.play.controller.api;

import com.myhome.play.model.network.Header;
import com.myhome.play.model.network.request.category.CategoryModifyRequest;
import com.myhome.play.model.network.response.category.CategoryListResponse;
import com.myhome.play.model.network.response.category.CategoryModifyResponse;
import com.myhome.play.service.CategoryApiService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class CategoryController {

    private CategoryApiService categoryApiService;

    public CategoryController(CategoryApiService categoryApiService) {
        this.categoryApiService = categoryApiService;
    }

    @GetMapping("/category")
    public Header<List<CategoryListResponse>> getList() {
        return Header.OK(categoryApiService.getCategoryList());
    }

    @PutMapping("/category")
    public Header<CategoryModifyResponse> modify(@RequestBody @Valid CategoryModifyRequest request){
        return categoryApiService.modify(request);
    }

    @DeleteMapping("/category/{id}")
    public Header delete(@PathVariable Long id){
        return categoryApiService.delete(id);
    }
}
