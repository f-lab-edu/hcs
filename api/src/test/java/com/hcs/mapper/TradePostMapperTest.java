package com.hcs.mapper;

import com.hcs.common.JdbcTemplateHelper;
import com.hcs.domain.TradePost;
import com.hcs.domain.User;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@EnableEncryptableProperties
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest(includeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = {".*DataSourceConfig", ".*JasyptConfig", ".*Helper"})})
class TradePostMapperTest {

    @Autowired
    UserMapper userMapper;

    @Autowired
    TradePostMapper tradePostMapper;

    @Autowired
    JdbcTemplateHelper jdbcTemplateHelper;

    @DisplayName("TradePostMapper - Id로 TradePost 찾기")
    @Test
    void findByIdTest() {

        String newEmail = "test@naver.com";
        String newNickname = "test";
        String newPassword = "password";

        long authorId = jdbcTemplateHelper.insertTestUser(newEmail, newNickname, newPassword);
        String title = "test";
        String productStatus = "중";
        String category = "중";
        String description = "중";
        int price = 10000;
        int salesStatus = 0;
        LocalDateTime registrationTime = LocalDateTime.now();

        long tradePostId = jdbcTemplateHelper.insertTestTradePost(authorId, title, productStatus, category, description, price, salesStatus, registrationTime);

        Optional<TradePost> returnedBy = Optional.ofNullable(tradePostMapper.findById(tradePostId));

        assertThat(returnedBy).isNotEmpty();
        assertThat(returnedBy.get().getId()).isEqualTo(tradePostId);
        assertThat(returnedBy.get().getTitle()).isEqualTo(title);
        assertThat(returnedBy.get().getProductStatus()).isEqualTo(productStatus);
        assertThat(returnedBy.get().getCategory()).isEqualTo(category);
        assertThat(returnedBy.get().getDescription()).isEqualTo(description);
        assertThat(returnedBy.get().getPrice()).isEqualTo(price);
    }

    @DisplayName("TradePostMapper - Title로 TradePost 찾기")
    @Test
    void findByTitleTest() {

        String newEmail = "test@naver.com";
        String newNickname = "test";
        String newPassword = "password";

        long authorId = jdbcTemplateHelper.insertTestUser(newEmail, newNickname, newPassword);
        String title = "test";
        String productStatus = "중";
        String category = "중";
        String description = "중";
        int price = 10000;
        int salesStatus = 0;
        LocalDateTime registrationTime = LocalDateTime.now();

        long tradePostId = jdbcTemplateHelper.insertTestTradePost(authorId, title, productStatus, category, description, price, salesStatus, registrationTime);

        Optional<TradePost> returnedBy = Optional.ofNullable(tradePostMapper.findByTitle(title));

        assertThat(returnedBy).isNotEmpty();
        assertThat(returnedBy.get().getId()).isEqualTo(tradePostId);
        assertThat(returnedBy.get().getTitle()).isEqualTo(title);
        assertThat(returnedBy.get().getProductStatus()).isEqualTo(productStatus);
        assertThat(returnedBy.get().getCategory()).isEqualTo(category);
        assertThat(returnedBy.get().getDescription()).isEqualTo(description);
        assertThat(returnedBy.get().getPrice()).isEqualTo(price);
    }

    @DisplayName("TradePostMapper - Id로 글쓴이 찾기")
    @Test
    void findAuthorByTradePostIdTest() {

        String newEmail = "test@naver.com";
        String newNickname = "test";
        String newPassword = "password";

        long authorId = jdbcTemplateHelper.insertTestUser(newEmail, newNickname, newPassword);
        String title = "test";
        String productStatus = "중";
        String category = "중";
        String description = "중";
        int price = 10000;
        int salesStatus = 0;
        LocalDateTime registrationTime = LocalDateTime.now();

        long tradePostId = jdbcTemplateHelper.insertTestTradePost(authorId, title, productStatus, category, description, price, salesStatus, registrationTime);
        Long returnedAuthorId = tradePostMapper.findAuthorIdById(tradePostId);

        Optional<User> returnedBy = Optional.ofNullable(userMapper.findById(returnedAuthorId));

        assertThat(returnedBy).isNotEmpty();
        assertThat(returnedBy.get().getId()).isEqualTo(authorId);
        assertThat(returnedBy.get().getEmail()).isEqualTo(newEmail);
        assertThat(returnedBy.get().getNickname()).isEqualTo(newNickname);
        assertThat(returnedBy.get().getPassword()).isEqualTo(newPassword);

        // TradePost의 Author가 아닌 경우
        Optional<User> returnedBy2 = Optional.ofNullable(userMapper.findById(returnedAuthorId + 1));

        assertThat(returnedBy2).isEmpty();
    }

    @DisplayName("TradePostMapper - TradePost 삽입하기")
    @Test
    void insertTradePostTest() {

        String newEmail = "test@naver.com";
        String newNickname = "test";
        String newPassword = "password";

        long authorId = jdbcTemplateHelper.insertTestUser(newEmail, newNickname, newPassword);

        User author = userMapper.findById(authorId);
        author.setId(authorId);

        String title = "test";
        String productStatus = "중";
        String category = "중";
        String description = "중";
        int price = 10000;
        boolean salesStatus = false;
        LocalDateTime registrationTime = LocalDateTime.now();

        TradePost tradePost = TradePost.builder()
                .title(title)
                .productStatus(productStatus)
                .category(category)
                .description(description)
                .price(price)
                .salesStatus(salesStatus)
                .registerationTime(registrationTime)
                .author(author)
                .build();

        tradePostMapper.insertTradePost(tradePost);

        long tradePostId = tradePost.getId();

        Optional<TradePost> returnedBy = Optional.ofNullable(tradePostMapper.findById(tradePostId));

        assertThat(returnedBy).isNotEmpty();
        assertThat(returnedBy.get().getId()).isEqualTo(tradePostId);
        assertThat(returnedBy.get().getTitle()).isEqualTo(title);
        assertThat(returnedBy.get().getProductStatus()).isEqualTo(productStatus);
        assertThat(returnedBy.get().getCategory()).isEqualTo(category);
        assertThat(returnedBy.get().getDescription()).isEqualTo(description);
        assertThat(returnedBy.get().getPrice()).isEqualTo(price);
    }

    @DisplayName("TradePostMapper - Id로 TradePost 삭제하기")
    @Test
    void deleteTradePostByIdTest() {

        String newEmail = "test@naver.com";
        String newNickname = "test";
        String newPassword = "password";

        long authorId = jdbcTemplateHelper.insertTestUser(newEmail, newNickname, newPassword);
        String title = "test";
        String productStatus = "중";
        String category = "중";
        String description = "중";
        int price = 10000;
        int salesStatus = 0;
        LocalDateTime registrationTime = LocalDateTime.now();

        long tradePostId = jdbcTemplateHelper.insertTestTradePost(authorId, title, productStatus, category, description, price, salesStatus, registrationTime);

        Optional<TradePost> returnedBy = Optional.ofNullable(tradePostMapper.findById(tradePostId));

        assertThat(returnedBy).isNotEmpty();

        int isSuccess = tradePostMapper.deleteTradePostById(tradePostId);

        assertThat(isSuccess).isGreaterThan(0);
    }
}
