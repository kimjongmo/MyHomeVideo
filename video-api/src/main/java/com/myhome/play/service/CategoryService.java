package com.myhome.play.service;

import com.myhome.play.exceptions.CategoryNotFoundException;
import com.myhome.play.model.entity.Category;
import com.myhome.play.model.network.request.category.CategoryInsertRequest;
import com.myhome.play.repo.CategoryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Value("${home.path}")
    public String HOME_PATH;

    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<String> getCategoryList() {
        List<Category> categoryList = categoryRepository.findAll();
        return categoryList.stream().map(category -> category.getName()).collect(Collectors.toList());
    }

    public Category insert(CategoryInsertRequest request) {
        File file = new File(HOME_PATH + "/" + request.getName());
        if (!file.exists())
            file.mkdir();
        return categoryRepository.save(Category.builder()
                        .name(request.getName())
                        .directoryPath(request.getName())
                        .build()
        );
    }
}
