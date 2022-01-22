package com.hcs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcs.annotation.EnableMockMvc;
import com.hcs.common.JdbcTemplateHelper;
import com.hcs.service.TradePostService;
import com.hcs.service.UserService;
import com.jayway.jsonpath.JsonPath;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@EnableMockMvc
@EnableEncryptableProperties
@EnableJpaRepositories(basePackages = {"com.hcs.repository"})
@Transactional
public class TradePostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserService userService;

    @Autowired
    TradePostService tradePostService;

    @Autowired
    JdbcTemplateHelper jdbcTemplateHelper;

    @Autowired
    ObjectMapper objectMapper;

    @DisplayName("중고거래 정보 요청시 리턴되는 body를 확인")
    @Test
    void tradePostInfoTest() throws Exception {

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

        MvcResult mvcResult = mockMvc.perform(get("/post/tradePost/info")
                        .param("tradePostId", String.valueOf(tradePostId)))

                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        int status = JsonPath.parse(response).read("$.HCS.status");
        HashMap<String, Object> item = JsonPath.parse(response).read("$.HCS.item");

        assertThat(status).isEqualTo(200);

        assertThat(item.get("tradePostId")).isEqualTo((int) tradePostId);
        assertThat(item.get("title")).isEqualTo(title);

        LinkedHashMap author = (LinkedHashMap) item.get("author");

        assertThat(author.get("userId")).isEqualTo((int) authorId);
        assertThat(author.get("email")).isEqualTo(newEmail);
        assertThat(author.get("nickname")).isEqualTo(newNickname);

        assertThat(item.get("productStatus")).isEqualTo(productStatus);
        assertThat(item.get("category")).isEqualTo(category);
        assertThat(item.get("description")).isEqualTo(description);
        assertThat(item.get("price")).isEqualTo(price);
    }

    @DisplayName("중고거래 페이지 요청시 리턴되는 body를 확인")
    @Test
    void tradePostListTest_with_paging() throws Exception {

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

            authorId = jdbcTemplateHelper.insertTestUser(newEmail + i, newNickname + i, newPassword + i, joinedAt);
            tradePostId = jdbcTemplateHelper.insertTestTradePost(authorId, title + i, productStatus + i, category, description + i, price + i, salesStatus, registrationTime.plusMinutes(i));

            authorIds[i] = authorId;
            tradePostIds[i] = tradePostId;
        }

        int page = 1;

        MvcResult mvcResult = mockMvc.perform(get("/post/tradePost/list")
                        .param("page", String.valueOf(page))
                        .param("category", category)
                        .param("salesStatus", String.valueOf(salesStatus)))

                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        int status = JsonPath.parse(response).read("$.HCS.status");
        HashMap<String, Object> item = JsonPath.parse(response).read("$.HCS.item");

        assertThat(status).isEqualTo(200);

        JSONArray tradePosts = (JSONArray) item.get("tradePosts");

        assertThat(item.get("page")).isEqualTo(page);
        assertThat(item.get("count")).isEqualTo(tradePosts.size());
        assertThat(item.get("salesStatus")).isEqualTo(Boolean.valueOf(String.valueOf(salesStatus)));
        assertThat(item.get("category")).isEqualTo(category);

        for (int i = 0; i < lng; i++) {

            int latestIdx = lng - i - 1;

            LinkedHashMap tradePost = (LinkedHashMap) tradePosts.get(i);

            assertThat((int) tradePost.get("tradePostId")).isEqualTo(tradePostIds[latestIdx]);
            assertThat(tradePost.get("title")).isEqualTo(title + latestIdx);

            LinkedHashMap author = (LinkedHashMap) tradePost.get("author");
            assertThat((int) author.get("userId")).isEqualTo(authorIds[latestIdx]);

            assertThat(tradePost.get("productStatus")).isEqualTo(productStatus + latestIdx);
            assertThat(tradePost.get("description")).isEqualTo(description + latestIdx);
            assertThat(tradePost.get("price")).isEqualTo(price + latestIdx);
        }
    }
}
