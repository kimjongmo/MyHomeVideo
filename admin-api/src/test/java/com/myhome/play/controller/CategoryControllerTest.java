package com.myhome.play.controller;

import com.myhome.play.controller.api.CategoryController;
import com.myhome.play.model.network.Header;
import com.myhome.play.model.network.request.category.CategoryModifyRequest;
import com.myhome.play.model.network.response.category.CategoryListResponse;
import com.myhome.play.service.CategoryApiService;
import com.myhome.play.utils.JsonMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private CategoryApiService categoryApiService;

    @Test
    //카테고리 리스트 테스트
    public void get_category_list() throws Exception {

        CategoryListResponse data = new CategoryListResponse();
        data.setId(1L);
        data.setName("TEST");

        List<CategoryListResponse> list = new ArrayList<>();
        list.add(data);

        given(categoryApiService.getCategoryList()).willReturn(list);

        mvc.perform(get("/category"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("OK")))
                .andExpect(content().string(containsString("1")))
                .andExpect(content().string(containsString("TEST")))
        ;
        verify(categoryApiService).getCategoryList();
    }

    @Test
    //카테고리 수정 시 입력데이터가 Null
    public void modify_category_test_with_null() throws Exception {
        CategoryModifyRequest request = CategoryModifyRequest.builder()
                .id(1L)
                .name("")
                .build();

        mvc.perform(put("/category")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonMapper.toJson(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("name")))
                .andExpect(content().string(containsString("필수")))
        ;
    }

    @Test
    // 카테고리 삭제 테스트
    public void delete_category_test() throws Exception {
        given(categoryApiService.delete(anyLong())).willReturn(Header.OK("삭제되었습니다"));
        mvc.perform(delete("/category/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("삭제")))
        ;
    }
}