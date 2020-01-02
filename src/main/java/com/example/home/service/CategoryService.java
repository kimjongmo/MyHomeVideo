package com.example.home.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Value("${home.path}")
    public String HOME_PATH = "";

    public List<String> getCategoryList() {
        File root = new File(HOME_PATH);
        File[] folderList = root.listFiles(pathname -> pathname.isDirectory());
        return Arrays.asList(folderList)
                .stream()
                .map(file -> file.getName())
                .collect(Collectors.toList());
    }

}
