package com.hcs.service;

import com.hcs.domain.Category;
import com.hcs.mapper.CategoryMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
        //given
        List<Category> givenCategoryList = new ArrayList<>();
        given(categoryMapper.selectAllCategory()).willReturn(givenCategoryList);

        //when
        List<Category> categoryList = categoryService.getAllCategory();

        //then
        assertEquals(categoryList, givenCategoryList);

    }

    @DisplayName("카테고리 이름이 주어지면, 그 이름에 해당하는 categoryId 반환하기")
    @ParameterizedTest
    @ValueSource(strings={"sports","reading","music"})
    void getCategoryId(String categoryName) {
        //given
        List<Category> givenCategoryList = new ArrayList<>();
        givenCategoryList.add(new Category(1L, "sports"));
        givenCategoryList.add(new Category(2L, "reading"));
        givenCategoryList.add(new Category(3L, "music"));
        given(categoryMapper.selectAllCategory()).willReturn(givenCategoryList);

        //when
        Long categoryId = categoryService.getCategoryId(categoryName);

        //then
        String givenCategoryName="";
        for (Category c: givenCategoryList) {
            if(categoryId.equals(c.getId()))
                givenCategoryName = c.getName();
        }
        assertEquals(categoryName,givenCategoryName);

    }
}
