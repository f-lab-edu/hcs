package com.hcs.mapper;

import com.hcs.domain.Category;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@EnableEncryptableProperties
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest(includeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*[DataSourceConfig]")})
public class CategoryMapperTest {

    @Autowired
    CategoryMapper categoryMapper;

    @DisplayName("모든 카테고리 가져오기")
    @Test
    void selectAllCategory() {
        int currentCategorySize = 10;
        List<Category> categoryList = categoryMapper.selectAllCategory();
        assertEquals(categoryList.size(), currentCategorySize);
        assertEquals(categoryList.get(0).getId(), 1);
        assertEquals(categoryList.get(0).getName(), "sports");
    }
}
