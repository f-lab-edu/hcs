package com.hcs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcs.annotation.EnableMockMvc;
import com.hcs.common.JdbcTemplateHelper;
import com.hcs.domain.TradePost;
import com.hcs.dto.request.TradePostDto;
import com.hcs.service.TradePostService;
import com.hcs.service.UserService;
import com.jayway.jsonpath.JsonPath;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@EnableMockMvc
@EnableEncryptableProperties
@EnableJpaRepositories(basePackages = {"com.hcs.repository"})
@Transactional
public class TradePostControllerTest {

    private static TradePostDto tradePostDto = new TradePostDto();

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

    static Stream<Arguments> stringListProvider() {
        return Stream.of(
                arguments("tes", "", "", "", "loca", "-181", "-91", "999", Arrays.asList("title", "category", "productStatus", "description", "locationName", "lng", "lat", "price")),
                arguments("tes", "중", "중", "중", "seoul station", "126.97230870958784", "37.55602954224621", "10000", Arrays.asList("title")),
                arguments("testTitle", "중", "중", "중", "cafe", "126.97230870958784", "37.55602954224621", "10000", Arrays.asList("locationName")),
                arguments("testTitle", "중", "중", "중", "seoul station", "126.97230870958784", "37.55602954224621", "10000000", Arrays.asList("price"))
        );
    }

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

    @DisplayName("중고거래 글 생성 처리 - 입력값 정상")
    @Test
    void saveTradePost_with_correct_input() throws Exception {

        String newEmail = "test@naver.com";
        String newNickname = "test";
        String newPassword = "password";
        LocalDateTime joinedAt = LocalDateTime.now();

        long authorId = jdbcTemplateHelper.insertTestUser(newEmail, newNickname, newPassword, joinedAt);
        String title = "testTitle";
        String productStatus = "중";
        String category = "중";
        String description = "중";
        int price = 10000;
        String locationName = "seoul station";
        double lng = 126.97230870958784;
        double lat = 37.55602954224621;

        tradePostDto = TradePostDto.builder()
                .title(title)
                .category(category)
                .productStatus(productStatus)
                .description(description)
                .locationName(locationName)
                .lng(lng)
                .lat(lat)
                .price(price)
                .build();

        MvcResult mvcResult = mockMvc.perform(post("/post/tradePost/submit")
                        .param("authorId", String.valueOf(authorId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tradePostDto))
                        .accept(MediaType.APPLICATION_JSON))

                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        int status = JsonPath.parse(response).read("$.HCS.status");
        HashMap<String, Object> item = JsonPath.parse(response).read("$.HCS.item");

        assertThat(status).isEqualTo(200);
        assertThat(item.get("authorId")).isEqualTo((int) authorId);

        TradePost tradePost = tradePostService.findByTitle(title);

        assertThat(item.get("tradePostId")).isEqualTo(tradePost.getId().intValue());
    }

    @DisplayName("중고거래 글 생성 처리 - 입력값 오류")
    @ParameterizedTest(name = "#{index} - {displayName} = Test with Argument={0}, {1}, {2}, {3}, {4}, {5}, {6}, {7}")
    @MethodSource("stringListProvider")
    void saveTradePost_with_wrong_input(String title, String productStatus, String category, String description,
                                        String locationName, double lng, double lat, int price, List<String> invalidFields) throws Exception {

        String newEmail = "test@naver.com";
        String newNickname = "test";
        String newPassword = "password";
        LocalDateTime joinedAt = LocalDateTime.now();

        long authorId = jdbcTemplateHelper.insertTestUser(newEmail, newNickname, newPassword, joinedAt);

        tradePostDto = TradePostDto.builder()
                .title(title)
                .category(category)
                .productStatus(productStatus)
                .description(description)
                .locationName(locationName)
                .lng(lng)
                .lat(lat)
                .price(price)
                .build();

        MvcResult mvcResult = mockMvc.perform(post("/post/tradePost/submit")
                        .param("authorId", String.valueOf(authorId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tradePostDto))
                        .accept(MediaType.APPLICATION_JSON))

                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        int status = JsonPath.parse(response).read("$.HCS.status");
        int length = JsonPath.parse(response).read("$.HCS.item.errors.length()");

        assertThat(status).isEqualTo(400);

        for (int i = 0; i < length; i++) {
            String field = JsonPath.parse(response).read("$.HCS.item.errors[" + i + "].field");
            assertThat(invalidFields).contains(field);
        }
    }
}
