package com.hcs.service;

import com.hcs.domain.Category;
import com.hcs.mapper.CategoryMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks
    CategoryService categoryService;

    @Mock
    CategoryMapper categoryMapper;

    @DisplayName("카테고리 리스트가 주어지면, 그 리스트 반환하기")
    @Test
    void getAllCategory(){
        List<Category> givenCategoryList = new ArrayList<>();
        //given
        given(categoryMapper.selectAllCategory()).willReturn(givenCategoryList);

        //when
        List<Category> categoryList = categoryService.getAllCategory();

        //then
        assertEquals(categoryList, givenCategoryList);

    }
}
