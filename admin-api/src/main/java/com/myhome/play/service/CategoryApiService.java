package com.myhome.play.service;

import com.myhome.play.model.entity.Category;
import com.myhome.play.model.network.Header;
import com.myhome.play.model.network.request.category.CategoryModifyRequest;
import com.myhome.play.model.network.response.category.CategoryListResponse;
import com.myhome.play.model.network.response.category.CategoryModifyResponse;
import com.myhome.play.repo.CategoryRepository;
import com.myhome.play.utils.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryApiService {

    private CategoryRepository categoryRepository;
    private FileUtils fileUtils;

    public CategoryApiService(CategoryRepository categoryRepository,
                              FileUtils fileUtils) {
        this.categoryRepository = categoryRepository;
        this.fileUtils = fileUtils;
    }

    public List<CategoryListResponse> getCategoryList() {
        List<Category> categoryList = categoryRepository.findAll();
        return categoryList.stream().map(category ->
                CategoryListResponse.builder().name(category.getName()).id(category.getId()).build()
        ).collect(Collectors.toList());
    }


    public Header<CategoryModifyResponse> modify(CategoryModifyRequest request) {
        Optional<Category> optional = categoryRepository.findById(request.getId());
        if (!optional.isPresent())
            return Header.ERROR("해당 카테고리는 등록되어 있지 않습니다");

        Category category = optional.get();

        //폴더가 존재하는지 확인 없으면 생성한다
        File folder = fileUtils.getCategory(category.getName());
        if (!folder.exists() || !folder.isDirectory())
            folder.mkdir(); //폴더 생성

        //데이터 수정 및 저장
        category.setName(request.getName());
        if (folder.renameTo(fileUtils.getCategory(request.getName()))) {
            Category saved = categoryRepository.save(category);
            return Header.OK(CategoryModifyResponse.of(saved));
        }

        //폴더 이름 수정에 실패했을 경우
        return Header.ERROR("이미 존재하는 카테고리는 아닌지 확인해주세요");
    }

    public Header delete(Long id) {

        Optional<Category> optional = categoryRepository.findById(id);
        if (!optional.isPresent())
            return Header.ERROR("존재하지 않는 카테고리입니다");

        Category category = optional.get();

        //카테고리 디렉토리를 가져온다.
        File directory = fileUtils.getCategory(category.getName());

        //카테고리 디렉토리가 실제 파일시스템에 존재한다면
        if (directory.exists() && directory.isDirectory()) {
            //디렉토리 안에 있는 파일을 모두 삭제
            for (File file : directory.listFiles()) {
                //디렉토리 안에 있는 파일 삭제 실패시
                if (!file.delete())
                    return Header.ERROR(file.getName() + " 파일 삭제 실패");
            }
            //파일을 모두 삭제 후 디렉토리 삭제
            if (!directory.delete())
                return Header.ERROR(directory.getName() + " 폴더 삭제 실패");
        }

        //DB 에서 데이터 삭제
        categoryRepository.deleteById(category.getId());

        return Header.MESSAGE("삭제되었습니다");
    }

}
