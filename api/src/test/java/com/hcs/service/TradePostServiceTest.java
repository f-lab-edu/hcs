package com.hcs.service;

import com.hcs.common.JdbcTemplateHelper;
import com.hcs.domain.TradePost;
import com.hcs.dto.request.TradePostDto;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EnableEncryptableProperties
@EnableJpaRepositories(basePackages = {"com.hcs.repository"})
@Transactional
class TradePostServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    TradePostService tradePostService;

    @Autowired
    JdbcTemplateHelper jdbcTemplateHelper;

    @Test
    @DisplayName("TradePostServiceTest - 중고거래 게시글 저장하기")
    void saveTradePostTest() {

        String newEmail = "test@naver.com";
        String newNickname = "test";
        String newPassword = "password";
        LocalDateTime joinedAt = LocalDateTime.now();

        long authorId = jdbcTemplateHelper.insertTestUser(newEmail, newNickname, newPassword, joinedAt);
        String title = "test";
        String productStatus = "중";
        String category = "중";
        String description = "중";
        int price = 10000;
        String locationName = "seoul station";
        double lng = 126.97230870958784;
        double lat = 37.55602954224621;

        TradePostDto tradePostDto = TradePostDto.builder()
                .title(title)
                .category(category)
                .productStatus(productStatus)
                .description(description)
                .locationName(locationName)
                .lng(lng)
                .lat(lat)
                .price(price)
                .build();

        TradePost tradePost = tradePostService.saveTradePost(authorId, tradePostDto);

        assertThat(tradePost).isEqualTo(tradePostService.findById(tradePost.getId()));
    }

    @Test
    @DisplayName("TradePostServiceTest - 중고거래 게시글 클릭 시 조회수 올리기")
    void clickTradePostTest() {

        String newEmail = "test@naver.com";
        String newNickname = "test";
        String newPassword = "password";
        LocalDateTime joinedAt = LocalDateTime.now();

        long authorId = jdbcTemplateHelper.insertTestUser(newEmail, newNickname, newPassword, joinedAt);
        String title = "test";
        String productStatus = "중";
        String category = "중";
        String description = "중";
        int price = 10000;
        int salesStatus = 0;
        LocalDateTime registrationTime = LocalDateTime.now();

        long tradePostId = jdbcTemplateHelper.insertTestTradePost(authorId, title, productStatus, category, description, price, salesStatus, registrationTime);

        int click = 5;
        for (int i = 0; i < click; i++) tradePostService.clickTradePost(tradePostId);

        TradePost tradePost = tradePostService.findById(tradePostId);

        assertThat(tradePost.getViews()).isEqualTo(click);
    }

    @Test
    @DisplayName("TradePostServiceTest - 중고거래 게시글 수정하기")
    void modifyTradePostTest() {

        String newEmail = "test@naver.com";
        String newNickname = "test";
        String newPassword = "password";
        LocalDateTime joinedAt = LocalDateTime.now();

        long authorId = jdbcTemplateHelper.insertTestUser(newEmail, newNickname, newPassword, joinedAt);
        String title = "test";
        String productStatus = "중";
        String category = "중";
        String description = "중";
        int price = 10000;
        int salesStatus = 0;
        LocalDateTime registrationTime = LocalDateTime.now();

        long tradePostId = jdbcTemplateHelper.insertTestTradePost(authorId, title, productStatus, category, description, price, salesStatus, registrationTime);

        String title2 = "test2";
        String productStatus2 = "중2";
        String category2 = "중2";
        String description2 = "중2";
        String locationName = "seoul station";
        double lng = 126.97230870958784;
        double lat = 37.55602954224621;
        int price2 = 20000;

        TradePostDto tradePostDto = TradePostDto.builder()
                .title(title2)
                .category(category2)
                .productStatus(productStatus2)
                .description(description2)
                .locationName(locationName)
                .lng(lng)
                .lat(lat)
                .price(price2)
                .build();

        long tradePostId2 = tradePostService.modifyTradePost(tradePostId, tradePostDto);
        TradePost tradePost = tradePostService.findById(tradePostId);

        assertThat(tradePost.getId()).isEqualTo(tradePostId2);
        assertThat(tradePost.getTitle()).isEqualTo(title2);
        assertThat(tradePost.getCategory()).isEqualTo(category2);
        assertThat(tradePost.getProductStatus()).isEqualTo(productStatus2);
        assertThat(tradePost.getDescription()).isEqualTo(description2);
        assertThat(tradePost.getPrice()).isEqualTo(price2);
    }

    @Test
    @DisplayName("TradePostServiceTest - 중고거래 게시글 리스트 정보를 내려주기")
    void findTradePostsWithPagingTest() {

        int lng = 7;

        String newEmail = "test@naver.com";
        String newNickname = "tes";
        String newPassword = "password";
        LocalDateTime joinedAt = LocalDateTime.now();

        String title = "test";
        String productStatus = "중";
        String category = "중";
        String description = "중";
        int price = 10000;
        int salesStatus = 0;
        LocalDateTime registrationTime = LocalDateTime.now();

        long authorId = 0;
        long tradePostId = 0;

        long[] authorIds = new long[lng];
        long[] tradePostIds = new long[lng];

        for (int i = 0; i < lng; i++) {

            authorId = jdbcTemplateHelper.insertTestUser(newEmail, newNickname, newPassword, joinedAt);
            tradePostId = jdbcTemplateHelper.insertTestTradePost(authorId, title, productStatus, category, description, price, salesStatus, registrationTime.plusMinutes(i));

            authorIds[i] = authorId;
            tradePostIds[i] = tradePostId;

            newEmail += i;
            newNickname += i;
            newPassword += i;

            title += i;
            productStatus += i;
            description += i;
            price += i;
        }

        List<TradePost> result = tradePostService.findTradePostsWithPaging(1, category, false);

        for (int i = 0; i < lng; i++) {

            int latestIdx = lng - i - 1;

            assertThat(result.get(i).getId()).isEqualTo((int) tradePostIds[latestIdx]);
            assertThat(result.get(i).getAuthor().getId()).isEqualTo((int) authorIds[latestIdx]);
        }
    }
}
