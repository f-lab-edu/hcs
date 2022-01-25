package com.hcs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcs.annotation.EnableMockMvc;
import com.hcs.common.JdbcTemplateHelper;
import com.hcs.domain.Comment;
import com.hcs.dto.request.CommentDto;
import com.hcs.exception.ErrorCode;
import com.hcs.mapper.CommentMapper;
import com.jayway.jsonpath.JsonPath;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@EnableMockMvc
@EnableEncryptableProperties
@EnableJpaRepositories(basePackages = {"com.hcs.repository"})
@Transactional
public class CommentControllerTest {

    private static CommentDto testCommentDto = new CommentDto();
    private static String ROOT_URL = "/post/tradePost/";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    JdbcTemplateHelper jdbcTemplateHelper;

    @DisplayName("댓글 정보 요청 및 리턴된 body가 잘 내려왔는지 확인")
    @Test
    void commentInfoTest() throws Exception {

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

        String contents = "(테스트용) 허용된 길이안으로 작성된 댓글입니다.";

        LocalDateTime comment_registerationTime = LocalDateTime.now();

        long commentId = jdbcTemplateHelper.insertTestComment(0, authorId, tradePostId, contents, comment_registerationTime);

        MvcResult mvcResult = mockMvc.perform(get(ROOT_URL + "/comment")
                        .param("tradePostId", String.valueOf(tradePostId))
                        .param("commentId", String.valueOf(commentId))
                        .accept(MediaType.APPLICATION_JSON))

                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        int status = JsonPath.parse(response).read("$.HCS.status");
        HashMap<String, Object> item = JsonPath.parse(response).read("$.HCS.item");

        assertThat(status).isEqualTo(200);

        assertThat(item.get("tradePostId")).isEqualTo((int) tradePostId);

        HashMap<String, Object> commentItem = JsonPath.parse(response).read("$.HCS.item.comment");

        assertThat(commentItem.get("commentId")).isEqualTo((int) commentId);
        assertThat(commentItem.get("authorId")).isEqualTo((int) authorId);
        assertThat(commentItem.get("contents")).isEqualTo(contents);
    }

    @DisplayName("댓글 페이지 요청시 리턴되는 body를 확인")
    @Test
    void commentsOntheTradePostTest_with_paging() throws Exception {

        int lng = 5;

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

        String contents = "(테스트용) 허용된 길이안으로 작성된 댓글입니다.";
        LocalDateTime comment_registerationTime = LocalDateTime.now();

        long commentId = 0;
        long[] commentIds = new long[lng];

        for (int i = 0; i < lng; i++) {

            commentId = jdbcTemplateHelper.insertTestComment(0, authorId, tradePostId, contents + i, comment_registerationTime.plusMinutes(i));

            commentIds[i] = commentId;
        }

        int page = 1;

        MvcResult mvcResult = mockMvc.perform(get(ROOT_URL + "/comment/list")
                        .param("page", String.valueOf(page))
                        .param("tradePostId", String.valueOf(tradePostId))
                        .accept(MediaType.APPLICATION_JSON))

                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        int status = JsonPath.parse(response).read("$.HCS.status");
        HashMap<String, Object> item = JsonPath.parse(response).read("$.HCS.item");

        assertThat(status).isEqualTo(200);

        JSONArray comments = (JSONArray) item.get("comments");

        assertThat(item.get("page")).isEqualTo(page);
        assertThat(item.get("count")).isEqualTo(comments.size());
        assertThat(item.get("tradePostId")).isEqualTo((int) tradePostId);

        for (int i = 0; i < lng; i++) {

            int latestIdx = lng - i - 1;

            LinkedHashMap comment = (LinkedHashMap) comments.get(i);

            assertThat((int) comment.get("commentId")).isEqualTo(commentIds[latestIdx]);
            assertThat(comment.get("authorId")).isEqualTo((int) authorId);
            assertThat(comment.get("contents")).isEqualTo(contents + latestIdx);
        }
    }

    @DisplayName("대댓글 페이지 요청시 리턴되는 body를 확인")
    @Test
    void replysOntheComment_with_paging() throws Exception {

        int lng = 5;

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

        String contents = "test 댓글내용";
        LocalDateTime comment_registerationTime = LocalDateTime.now();
        long parentCommentId = jdbcTemplateHelper.insertTestComment(0, authorId, tradePostId, contents, comment_registerationTime);

        String reply_contents = "test 대댓글내용";
        LocalDateTime reply_registerationTime = LocalDateTime.now();

        long replyId = 0;
        long[] replyIds = new long[lng];

        for (int i = 0; i < lng; i++) {

            replyId = jdbcTemplateHelper.insertTestComment(parentCommentId, authorId, tradePostId, reply_contents + i, reply_registerationTime.plusMinutes(i));
            replyIds[i] = replyId;
        }

        int page = 1;

        MvcResult mvcResult = mockMvc.perform(get(ROOT_URL + "/comment/reply/list")
                        .param("page", String.valueOf(page))
                        .param("tradePostId", String.valueOf(tradePostId))
                        .param("parentCommentId", String.valueOf(parentCommentId))
                        .accept(MediaType.APPLICATION_JSON))

                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        int status = JsonPath.parse(response).read("$.HCS.status");
        HashMap<String, Object> item = JsonPath.parse(response).read("$.HCS.item");

        assertThat(status).isEqualTo(200);

        JSONArray replys = (JSONArray) item.get("replys");

        assertThat(item.get("page")).isEqualTo(page);
        assertThat(item.get("count")).isEqualTo(replys.size());
        assertThat(item.get("tradePostId")).isEqualTo((int) tradePostId);
        assertThat(item.get("parentCommentId")).isEqualTo((int) parentCommentId);

        for (int i = 0; i < lng; i++) {

            int latestIdx = lng - i - 1;

            LinkedHashMap reply = (LinkedHashMap) replys.get(i);

            assertThat((int) reply.get("commentId")).isEqualTo(replyIds[latestIdx]);
            assertThat(reply.get("authorId")).isEqualTo((int) authorId);
            assertThat(reply.get("contents")).isEqualTo(reply_contents + latestIdx);
        }
    }

    @DisplayName("댓글 달기 - commentDto 정상")
    @Test
    void addComment_with_correct_commentDto() throws Exception {

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

        String contents = "(테스트용) 허용된 길이안으로 작성된 댓글입니다.";

        testCommentDto.setContents(contents);

        MvcResult mvcResult = mockMvc.perform(post(ROOT_URL + "/comment")
                        .param("authorId", String.valueOf(authorId))
                        .param("tradePostId", String.valueOf(tradePostId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCommentDto))
                        .accept(MediaType.APPLICATION_JSON))

                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        List<Long> commentIds = commentMapper.findByTradePostId(tradePostId).stream().map(comment -> comment.getId()).collect(toList());

        int status = JsonPath.parse(response).read("$.HCS.status");
        HashMap<String, Object> item = JsonPath.parse(response).read("$.HCS.item");

        assertThat(status).isEqualTo(200);
        assertThat(item.get("tradePostId")).isEqualTo((int) tradePostId);
        assertThat(item.get("commentId")).isIn(commentIds.stream().map(c -> c.intValue()).collect(toList()));
    }

    @DisplayName("댓글 달기 - commentDto 오류 - 허용된 댓글 길이 초과")
    @Test
    void addComment_with_wrong_commentDto() throws Exception {

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

        String contents = "(테스트용) 허용된 길이안으로 작성된 댓글입니다.".repeat(30);

        testCommentDto.setContents(contents);

        MvcResult mvcResult = mockMvc.perform(post(ROOT_URL + "/comment")
                        .param("authorId", String.valueOf(authorId))
                        .param("tradePostId", String.valueOf(tradePostId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCommentDto))
                        .accept(MediaType.APPLICATION_JSON))

                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        int status = JsonPath.parse(response).read("$.HCS.status");
        HashMap<String, Object> item = JsonPath.parse(response).read("$.HCS.item");

        ErrorCode error = ErrorCode.METHOD_ARGUMENT_NOT_VALID;

        assertThat(status).isEqualTo(error.getStatus());
        assertThat(item.get("errorCode")).isEqualTo(error.getErrorCode());
        assertThat(item.get("message")).isEqualTo(error.getMessage());

        String field = JsonPath.parse(response).read("$.HCS.item.errors[0].field");
        String code = JsonPath.parse(response).read("$.HCS.item.errors[0].code");
        String message = JsonPath.parse(response).read("$.HCS.item.errors[0].message");

        assertThat(field).isEqualTo("contents");
        assertThat(code).isEqualTo("Length");
        assertThat(message).isEqualTo("길이가 5에서 200 사이여야 합니다");
    }

    @DisplayName("대댓글 달기 - commentDto 정상")
    @Test
    void addReplyOnComment_with_correct_commentDto() throws Exception {

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

        String contents = "(테스트용) 허용된 길이안으로 작성된 댓글입니다.";

        LocalDateTime comment_registerationTime = LocalDateTime.now();

        long parentCommentId = jdbcTemplateHelper.insertTestComment(0, authorId, tradePostId, contents, comment_registerationTime);

        String reply_contents = "(테스트용) 허용된 길이안으로 작성된 대댓글입니다.";

        testCommentDto.setContents(reply_contents);

        MvcResult mvcResult = mockMvc.perform(post(ROOT_URL + "/comment/reply")
                        .param("authorId", String.valueOf(authorId))
                        .param("tradePostId", String.valueOf(tradePostId))
                        .param("parentCommentId", String.valueOf(parentCommentId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCommentDto))
                        .accept(MediaType.APPLICATION_JSON))

                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        List<Long> replyIds = commentMapper.findReplysByParentCommentId(parentCommentId).stream().map(comment -> comment.getId()).collect(toList());

        int status = JsonPath.parse(response).read("$.HCS.status");
        HashMap<String, Object> item = JsonPath.parse(response).read("$.HCS.item");

        assertThat(status).isEqualTo(200);
        assertThat(item.get("tradePostId")).isEqualTo((int) tradePostId);
        assertThat(item.get("parentCommentId")).isEqualTo((int) parentCommentId);
        assertThat(item.get("replyId")).isIn(replyIds.stream().map(c -> c.intValue()).collect(toList()));
    }

    @DisplayName("댓글 수정 - commentDto 정상")
    @Test
    void modifyComment_with_correct_commentDto() throws Exception {

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

        String contents = "(테스트용) 허용된 길이안으로 작성된 댓글입니다.";

        LocalDateTime comment_registerationTime = LocalDateTime.now();

        long commentId = jdbcTemplateHelper.insertTestComment(0, authorId, tradePostId, contents, comment_registerationTime);

        String modify_contents = "(테스트용) 허용된 길이안으로 수정된 댓글입니다.";

        testCommentDto.setContents(modify_contents);

        MvcResult mvcResult = mockMvc.perform(put(ROOT_URL + "/comment/")
                        .param("tradePostId", String.valueOf(tradePostId))
                        .param("commentId", String.valueOf(commentId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCommentDto))
                        .accept(MediaType.APPLICATION_JSON))

                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        Comment comment = commentMapper.findById(commentId);

        assertThat(comment.getContents()).isEqualTo(modify_contents);

        int status = JsonPath.parse(response).read("$.HCS.status");
        HashMap<String, Object> item = JsonPath.parse(response).read("$.HCS.item");

        assertThat(status).isEqualTo(200);
        assertThat(item.get("tradePostId")).isEqualTo((int) tradePostId);
        assertThat(item.get("commentId")).isEqualTo((int) comment.getId());
    }

    @DisplayName("댓글 수정 - commentDto 오류 - 허용된 댓글 길이 초과")
    @Test
    void modifyComment_with_wrong_commentDto() throws Exception {

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

        String contents = "(테스트용) 허용된 길이안으로 작성된 댓글입니다.";

        LocalDateTime comment_registerationTime = LocalDateTime.now();

        long commentId = jdbcTemplateHelper.insertTestComment(0, authorId, tradePostId, contents, comment_registerationTime);

        String modify_contents = "(테스트용) 허용된 길이가 초과되어 수정된 댓글입니다.".repeat(30);

        testCommentDto.setContents(modify_contents);

        MvcResult mvcResult = mockMvc.perform(put(ROOT_URL + "/comment/")
                        .param("tradePostId", String.valueOf(tradePostId))
                        .param("commentId", String.valueOf(commentId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCommentDto))
                        .accept(MediaType.APPLICATION_JSON))

                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        int status = JsonPath.parse(response).read("$.HCS.status");
        HashMap<String, Object> item = JsonPath.parse(response).read("$.HCS.item");

        ErrorCode error = ErrorCode.METHOD_ARGUMENT_NOT_VALID;

        assertThat(status).isEqualTo(error.getStatus());
        assertThat(item.get("errorCode")).isEqualTo(error.getErrorCode());
        assertThat(item.get("message")).isEqualTo(error.getMessage());

        String field = JsonPath.parse(response).read("$.HCS.item.errors[0].field");
        String code = JsonPath.parse(response).read("$.HCS.item.errors[0].code");
        String message = JsonPath.parse(response).read("$.HCS.item.errors[0].message");

        assertThat(field).isEqualTo("contents");
        assertThat(code).isEqualTo("Length");
        assertThat(message).isEqualTo("길이가 5에서 200 사이여야 합니다");
    }

    @DisplayName("댓글 삭제")
    @Test
    void deleteCommentTest() throws Exception {

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

        String contents = "(테스트용) 허용된 길이안으로 작성된 댓글입니다.";

        LocalDateTime comment_registerationTime = LocalDateTime.now();

        long commentId = jdbcTemplateHelper.insertTestComment(0, authorId, tradePostId, contents, comment_registerationTime);

        MvcResult mvcResult = mockMvc.perform(delete(ROOT_URL + "/comment/")
                        .param("tradePostId", String.valueOf(tradePostId))
                        .param("commentId", String.valueOf(commentId))
                        .accept(MediaType.APPLICATION_JSON))

                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Optional<Comment> returned = Optional.ofNullable(commentMapper.findById(commentId));
        assertThat(returned.isPresent()).isFalse();

        String response = mvcResult.getResponse().getContentAsString();

        int status = JsonPath.parse(response).read("$.HCS.status");
        HashMap<String, Object> item = JsonPath.parse(response).read("$.HCS.item");

        assertThat(status).isEqualTo(200);
        assertThat(item.get("tradePostId")).isEqualTo((int) tradePostId);
        assertThat(item.get("commentId")).isEqualTo((int) commentId);
    }
}
