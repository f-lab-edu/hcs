package com.hcs.service;

import com.hcs.domain.Category;
import com.hcs.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Cacheable : 메소드의 결과를 캐시하고, 같은 key 값으로 메소드 실행 시 캐시된 데이터를 반환한다.
 */

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryMapper categoryMapper;

    @Cacheable(value = "categories")
    public List<Category> getAllCategory() {
        return categoryMapper.selectAllCategory();
    }

    @Cacheable(value = "categoryId", key = "#categoryName")
    public long getCategoryId(String categoryName) {
        List<Category> categoryList = getAllCategory();
        for (Category category : categoryList) {
            if (category.getName().equals(categoryName)) {
                return category.getId();
            }
        }
        return 0; //TODO : 에러 추가
    }

    @Cacheable(value = "categoryName", key = "#categoryId")
    public String getCategoryName(long categoryId) {
        List<Category> categoryList = getAllCategory();
        for (Category category : categoryList) {
            if (category.getId() == categoryId) {
                return category.getName();
            }
        }
        return null; //TODO : 에러 추가
    }
}
