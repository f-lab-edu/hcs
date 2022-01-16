package com.hcs.mapper;

import com.hcs.domain.TradePost;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@EnableEncryptableProperties
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest(includeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = {".*DataSourceConfig", ".*JasyptConfig"})})
class TradePostMapperTest {

    @Autowired
    TradePostMapper tradePostMapper;

    @Autowired
    JdbcTemplate jdbcTemplate;

    void insertTestTradePost(long authorId, String title, String productStatus, String category, String description, int price, LocalDateTime registerationTime) {

        String insertSql = "insert into TradePost (authorId, title, productStatus, category, description, price, registerationTime)\n" +
                "values (?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(insertSql, new Object[]{authorId, title, productStatus, category, description, price, registerationTime});
    }


    @DisplayName("TradePostMapper - Title로 TradePost 찾기")
    @Test
    void findByTitleTest() {

        long authorId = 43;
        String title = "test";
        String productStatus = "중";
        String category = "중";
        String description = "중";
        int price = 10000;
        LocalDateTime registrationTime = LocalDateTime.now();

        insertTestTradePost(authorId, title, productStatus, category, description, price, registrationTime);

        Optional<TradePost> returnedBy = Optional.ofNullable(tradePostMapper.findByTitle(title));

        assertThat(returnedBy).isNotEmpty();
        assertThat(returnedBy.get().getTitle()).isEqualTo(title);
        assertThat(returnedBy.get().getProductStatus()).isEqualTo(productStatus);
        assertThat(returnedBy.get().getCategory()).isEqualTo(category);
        assertThat(returnedBy.get().getDescription()).isEqualTo(description);
        assertThat(returnedBy.get().getPrice()).isEqualTo(price);
    }

    @DisplayName("TradePostMapper - Id로 TradePost 찾기")
    @Test
    void findByIdTest() {

        long authorId = 43;
        String title = "test";
        String productStatus = "중";
        String category = "중";
        String description = "중";
        int price = 10000;
        LocalDateTime registrationTime = LocalDateTime.now();

        insertTestTradePost(authorId, title, productStatus, category, description, price, registrationTime);

        Optional<TradePost> insertedTradePost = Optional.ofNullable(tradePostMapper.findByTitle(title));
        Optional<TradePost> returnedBy = Optional.ofNullable(tradePostMapper.findById(insertedTradePost.get().getId()));

        assertThat(returnedBy).isNotEmpty();
        assertThat(returnedBy.get().getTitle()).isEqualTo(title);
        assertThat(returnedBy.get().getProductStatus()).isEqualTo(productStatus);
        assertThat(returnedBy.get().getCategory()).isEqualTo(category);
        assertThat(returnedBy.get().getDescription()).isEqualTo(description);
        assertThat(returnedBy.get().getPrice()).isEqualTo(price);
    }
}
