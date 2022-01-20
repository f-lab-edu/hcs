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

    @DisplayName("TradePostMapper - 제목으로 TradePost 수 찾기")
    @Test
    void countByTitleTest() {

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

        jdbcTemplateHelper.insertTestTradePost(authorId, title, productStatus, category, description, price, salesStatus, registrationTime);
        jdbcTemplateHelper.insertTestTradePost(authorId, title, productStatus, category, description, price, salesStatus, registrationTime);
        jdbcTemplateHelper.insertTestTradePost(authorId, title, productStatus, category, description, price, salesStatus, registrationTime);

        int count = tradePostMapper.countByTitle(title);

        assertThat(count).isEqualTo(3);
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

    @DisplayName("TradePostMapper - TradePost 수정하기")
    @Test
    void updateTradePostTest() {

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
        int salesStatus = 0;
        LocalDateTime registrationTime = LocalDateTime.now();

        long tradePostId = jdbcTemplateHelper.insertTestTradePost(authorId, title, productStatus, category, description, price, salesStatus, registrationTime);

        TradePost modified = TradePost.builder()
                .id(tradePostId)
                .title(title)
                .productStatus(productStatus)
                .category(category)
                .description(description + 1)
                .price(price + 1)
                .salesStatus(Boolean.parseBoolean(String.valueOf(salesStatus)))
                .registerationTime(LocalDateTime.now())
                .author(author)
                .build();

        int isSuccess = tradePostMapper.updateTradePost(modified);

        assertThat(isSuccess).isGreaterThan(0);
        assertThat(modified.getId()).isEqualTo(tradePostId);
        assertThat(modified.getDescription()).isEqualTo(description + 1);
        assertThat(modified.getPrice()).isEqualTo(price + 1);
    }

    @DisplayName("TradePostMapper - TradePost 클릭시 조회수 올리기")
    @Test
    void updateTradePostForViewTest() {

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
        int salesStatus = 0;
        LocalDateTime registrationTime = LocalDateTime.now();

        long tradePostId = jdbcTemplateHelper.insertTestTradePost(authorId, title, productStatus, category, description, price, salesStatus, registrationTime);

        TradePost tradePost = tradePostMapper.findById(tradePostId);

        int isSuccess = tradePostMapper.updateTradePostForView(tradePostId);

        TradePost clickedTradePost = tradePostMapper.findById(tradePostId);

        assertThat(isSuccess).isGreaterThan(0);
        assertThat(clickedTradePost.getViews()).isEqualTo(tradePost.getViews() + 1);
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
